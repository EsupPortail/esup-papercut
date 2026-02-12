package org.esupportail.papercut.config;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@FilterDef(name = "contextFilter", parameters = {@ParamDef(name = "papercutContext", type = String.class)})
public class HibernateFilterConfig {
}

