package com.nemezis.cloth.view;

import com.nemezis.cloth.model.Application;

import java.util.List;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
public interface ApplicationsView extends IView {
    void displayApplications(List<Application> applications);
}
