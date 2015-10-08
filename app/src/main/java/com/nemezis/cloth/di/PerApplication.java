package com.nemezis.cloth.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerApplication {}
