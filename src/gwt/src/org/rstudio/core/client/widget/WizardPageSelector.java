/*
 * WizardPageSelector.java
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
package org.rstudio.core.client.widget;

import java.util.ArrayList;

import org.rstudio.core.client.CommandWithArg;


import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;

public class WizardPageSelector<I,T> extends Composite
{
   public WizardPageSelector(ArrayList<WizardPage<I,T>> pages,
                             final CommandWithArg<WizardPage<I,T>> onSelected)
   {
      WizardResources res = WizardResources.INSTANCE;
      WizardResources.Styles styles = res.styles();
      
      FlowPanel pageSelectorPanel = new FlowPanel();
      pageSelectorPanel.addStyleName(styles.wizardPageSelector());
      pageSelectorPanel.setSize("100%", "100%");
      for (int i=0; i<pages.size(); i++)
      {
         final WizardPage<I,T> page = pages.get(i);
         PageSelectorItem pageSelector = 
           new PageSelectorItem(page, new ClickHandler() {
              @Override
              public void onClick(ClickEvent event)
              {
                onSelected.execute(page);  
              }
           });
         
         if (i==0)
            pageSelector.addStyleName(styles.wizardPageSelectorItemFirst());
         
         if (i == (pages.size() -1))
            pageSelector.addStyleName(styles.wizardPageSelectorItemLast());
         
         pageSelectorPanel.add(pageSelector);
      }
      
      initWidget(pageSelectorPanel);
   }
   
   private class PageSelectorItem extends Composite
   {
      PageSelectorItem(final WizardPageInfo pageInfo, ClickHandler clickHandler)
      {
         WizardResources res = WizardResources.INSTANCE;
         WizardResources.Styles styles = res.styles();
         
         LayoutPanel layoutPanel = new LayoutPanel();
         layoutPanel.addStyleName(styles.wizardPageSelectorItem());
         
         Image image = new Image(pageInfo.getImage());
         layoutPanel.add(image);
         layoutPanel.setWidgetLeftWidth(image, 
                                        10, Unit.PX, 
                                        image.getWidth(), Unit.PX);
         layoutPanel.setWidgetTopHeight(image, 
                                        40-(image.getHeight()/2), Unit.PX, 
                                        image.getHeight(), Unit.PX);
         
        
         FlowPanel captionPanel = new FlowPanel();
         Label titleLabel = new Label(pageInfo.getTitle());
         titleLabel.addStyleName(styles.headerLabel());
         captionPanel.add(titleLabel);
         Label subTitleLabel = new Label(pageInfo.getSubTitle());
         subTitleLabel.addStyleName(styles.subcaptionLabel());
         captionPanel.add(subTitleLabel);
         layoutPanel.add(captionPanel);
         layoutPanel.setWidgetLeftWidth(captionPanel,
                                        10 + image.getWidth() + 12, Unit.PX,
                                        450, Unit.PX);
         layoutPanel.setWidgetTopHeight(captionPanel,
                                        19, Unit.PX, 
                                        55, Unit.PX);
         
         
         Image arrowImage = new Image(res.wizardDisclosureArrow());
         layoutPanel.add(arrowImage);
         layoutPanel.setWidgetRightWidth(arrowImage, 
                                         20, Unit.PX, 
                                         arrowImage.getWidth(), 
                                         Unit.PX);
         layoutPanel.setWidgetTopHeight(arrowImage,
                                        40-(arrowImage.getHeight()/2), Unit.PX,
                                        arrowImage.getHeight(), Unit.PX);
         
         
         layoutPanel.addDomHandler(clickHandler, ClickEvent.getType());
       
         
         initWidget(layoutPanel);
      }
   }
}