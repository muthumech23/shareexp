/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.shareexpense.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AH0661755
 */
public class CommonUtil {

    public static boolean checkStringNullBlank(String string) {

        if (string == null || string.isEmpty() || string.trim().equalsIgnoreCase("")) {
            return true;
        }

        return false;
    }

    public static <E> List<E> toList(Iterable<E> iterable) {
        if (iterable instanceof List) {
            return (List<E>) iterable;
        }
        ArrayList<E> list = new ArrayList<E>();
        if (iterable != null) {
            for (E e : iterable) {
                list.add(e);
            }
        }
        return list;
    }

}
