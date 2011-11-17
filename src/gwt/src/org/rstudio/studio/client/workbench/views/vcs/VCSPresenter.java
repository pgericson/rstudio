/*
 * VCSPresenter.java
 *
 * Copyright (C) 2009-11 by RStudio, Inc.
 *
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client.workbench.views.vcs;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.rstudio.studio.client.workbench.model.Session;
import org.rstudio.studio.client.workbench.views.BasePresenter;
import org.rstudio.studio.client.workbench.views.vcs.svn.SVNPresenter;

public class VCSPresenter implements IsWidget
{
   @Inject
   public VCSPresenter(Session session,
                       Provider<GitPresenter> pGitPresenter,
                       Provider<SVNPresenter> pSVNPresenter)
   {
      session_ = session;
      pGitPresenter_ = pGitPresenter;
      pSVNPresenter_ = pSVNPresenter;
   }

   @Override
   public Widget asWidget()
   {
      ensurePresenterCreated();

      return presenter_.asWidget();
   }

   private void ensurePresenterCreated()
   {
      if (presenter_ == null)
      {
         String vcsName = session_.getSessionInfo().getVcsName();
         if ("git".equalsIgnoreCase(vcsName))
            presenter_ = pGitPresenter_.get();
         else if ("subversion".equalsIgnoreCase(vcsName))
            presenter_ = pSVNPresenter_.get();
      }
   }

   public void onBeforeSelected()
   {
      presenter_.onBeforeSelected();
   }

   public void onSelected()
   {
      presenter_.onSelected();
   }

   private final Session session_;
   private final Provider<GitPresenter> pGitPresenter_;
   private final Provider<SVNPresenter> pSVNPresenter_;
   private BasePresenter presenter_;
}