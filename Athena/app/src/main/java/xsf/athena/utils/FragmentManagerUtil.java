package xsf.athena.utils;

import java.util.HashMap;
import java.util.Map;

import xsf.athena.fragment.base.BaseFragment;

/**
 * Author: xsf
 * Time: created at 2016/4/23.
 * Email: xsf_uestc_ncl@163.com
 */
public class FragmentManagerUtil {
    private static Map<String, BaseFragment> fragmentlist = new HashMap<>();

    public static BaseFragment createFragment(Class<?> clazz, boolean isAddList) {
        BaseFragment targetFrament = null;
        String className = clazz.getName();
        if (fragmentlist.containsKey(className)) {
            targetFrament = fragmentlist.get(className);
        } else {
            try {
                targetFrament = (BaseFragment) Class.forName(className).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (isAddList) {
            fragmentlist.put(className, targetFrament);
        }
        return targetFrament;
    }

    public static BaseFragment createFragment(Class<?> clazz) {
        return createFragment(clazz, true);
    }

    public static void clearFragmentList() {
        fragmentlist.clear();
    }
}
