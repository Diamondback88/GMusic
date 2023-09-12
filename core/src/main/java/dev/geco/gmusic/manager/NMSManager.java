package dev.geco.gmusic.manager;

import java.lang.reflect.*;

import org.bukkit.*;

public class NMSManager {

    private static Class<?>[] toPrimitiveTypeArray(Class<?>[] Classes) {
        int length = Classes != null ? Classes.length : 0;
        Class<?>[] type = new Class<?>[length];
        for(int count = 0; count < length; count++) type[count] = Classes[count];
        return type;
    }

    private static boolean equalsTypeArray(Class<?>[] Type, Class<?>[] OtherType) {
        if(Type.length != OtherType.length) return false;
        for(int count = 0; count < Type.length; count++) if(!Type[count].equals(OtherType[count]) && !Type[count].isAssignableFrom(OtherType[count])) return false;
        return true;
    }

    public static String getClassVersion() {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        return version.substring(version.lastIndexOf('.') + 1);
    }

    public static String getPackageVersion() {
        return getClassVersion() + (isVersion(17, 1) || isVersion(19, 1) || isVersion(19, 2) ? "_2" : "");
    }

    public static String getVersion() { return Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf('-')); }

    public static boolean isNewerOrVersion(int Version, int SubVersion) {
        String[] version = getVersion().split("\\.");
        return Integer.parseInt(version[1]) > Version || (Integer.parseInt(version[1]) == Version && (version.length > 2 ? Integer.parseInt(version[2]) >= SubVersion : SubVersion == 0));
    }

    public static boolean isVersion(int Version, int SubVersion) {
        String[] version = getVersion().split("\\.");
        return version.length > 2 ? Integer.parseInt(version[1]) == Version && Integer.parseInt(version[2]) == SubVersion : Integer.parseInt(version[1]) == Version && SubVersion == 0;
    }

    public static Object getPackageObject(String ClassName, Object Object) {
        try {
            String packageName = NMSManager.class.getPackage().getName();
            Class<?> mcvClass = Class.forName(packageName.substring(0, packageName.lastIndexOf('.')) + ".mcv." + NMSManager.getPackageVersion() + "." + ClassName);
            return Object == null ? mcvClass.getConstructor().newInstance() : mcvClass.getConstructor(Object.getClass()).newInstance(Object);
        } catch (Throwable e) { return null; }
    }

    public static boolean hasPackageClass(String ClassName) {
        try {
            String packageName = NMSManager.class.getPackage().getName();
            Class.forName(packageName.substring(0, packageName.lastIndexOf('.')) + ".mcv." + NMSManager.getPackageVersion() + "." + ClassName);
            return true;
        } catch (Throwable e) { return false; }
    }

    public static Method getMethod(String MethodName, Class<?> Class, Class<?>... Parameters) {
        Class<?>[] type = toPrimitiveTypeArray(Parameters);
        for(Method method : Class.getMethods()) if(method.getName().equals(MethodName) && equalsTypeArray(toPrimitiveTypeArray(method.getParameterTypes()), type)) return method;
        return null;
    }

    public static Object getHandle(Object Object) {
        try {
            Method getHandle = Object.getClass().getDeclaredMethod("getHandle");
            getHandle.setAccessible(true);
            return getHandle.invoke(Object);
        } catch (Throwable e) { e.printStackTrace(); }
        return null;
    }

}