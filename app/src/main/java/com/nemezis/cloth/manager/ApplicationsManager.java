package com.nemezis.cloth.manager;

import com.nemezis.cloth.App;
import com.nemezis.cloth.service.FabricService;

import javax.inject.Inject;

/**
 * Created by Dmitry Kostyrev on 18/10/15
 */
public class ApplicationsManager {

    @Inject
    FabricService fabricService;

    public ApplicationsManager(App applicationContext) {
        applicationContext.getApplicationComponent().inject(this);
    }


}