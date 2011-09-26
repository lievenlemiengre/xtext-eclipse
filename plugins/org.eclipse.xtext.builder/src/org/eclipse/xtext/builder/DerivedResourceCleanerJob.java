/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.builder;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.xtext.builder.internal.Activator;
import org.eclipse.xtext.ui.XtextProjectHelper;

import com.google.inject.Inject;

/**
 * @author Michael Clay - Initial contribution and API
 * @since 2.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class DerivedResourceCleanerJob extends Job {
	public static final Object DERIVED_RESOURCE_CLEANER_JOB_FAMILY = new Object();
	private DerivedResourceMarkers derivedResourceMarkers;
	private IProject projectToClean;
	private String folderNameToClean;

	public DerivedResourceMarkers getDerivedResourceMarkers() {
		return derivedResourceMarkers;
	}

	@Inject
	public void setDerivedResourceMarkers(DerivedResourceMarkers derivedResourceMarkers) {
		this.derivedResourceMarkers = derivedResourceMarkers;
	}

	public DerivedResourceCleanerJob() {
		super(Messages.DerivedResourceCleanerJob_JOB_NAME);
	}

	@Override
	public boolean belongsTo(Object family) {
		return family == DERIVED_RESOURCE_CLEANER_JOB_FAMILY;
	}

	public void initialize(IProject projectToClean, String folderNameToClean) {
		this.projectToClean = projectToClean;
		this.folderNameToClean = folderNameToClean;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		try {
			new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException,
						InterruptedException {
					if (projectToClean == null) {
						for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
							IStatus status = cleanUpDerivedResources(monitor, project);
							if (status != Status.OK_STATUS) {
								throw new CoreException(status);
							}
						}
					} else {
						final IStatus status = cleanUpDerivedResources(monitor, projectToClean);
						if (status != Status.OK_STATUS) {
							throw new CoreException(status);
						}
					}
				}
			}.run(monitor);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof CoreException) {
				return ((CoreException)e.getTargetException()).getStatus();
			} else {
				Activator.log(e);
			}
		} catch (InterruptedException e) {
			Activator.log(e);
		}
		return Status.OK_STATUS;
	}

	protected IStatus cleanUpDerivedResources(IProgressMonitor monitor, IProject project) throws CoreException {
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		if (shouldBeProcessed(project)) {
			for (IFile derivedFile : derivedResourceMarkers.findDerivedResources(project.getFolder(folderNameToClean),
					null)) {
				derivedFile.delete(true, monitor);
//				deleteEmptyParent(monitor, derivedFile.getParent());
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
			}
		}
		return Status.OK_STATUS;
	}

	protected void deleteEmptyParent(IProgressMonitor monitor, IContainer container) throws CoreException {
		final IContainer parent = container.getParent();
		if (container.members().length == 0) {
			container.delete(true, monitor);
			deleteEmptyParent(monitor, parent);
		}
	}

	protected boolean shouldBeProcessed(IProject project) {
		return XtextProjectHelper.hasNature(project) && project.getFolder(folderNameToClean).exists();
	}

}