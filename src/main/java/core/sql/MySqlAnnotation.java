/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.sql;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Antimo
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface MySqlAnnotation {   
    int type();   
}
 