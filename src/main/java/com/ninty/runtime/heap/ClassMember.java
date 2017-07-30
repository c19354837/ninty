package com.ninty.runtime.heap;

import com.ninty.classfile.MemberInfo;

/**
 * Created by ninty on 2017/7/23.
 */
public class ClassMember {

    protected int accessFlags;
    protected String name;
    protected String desc;
    protected NiClass clz;

    void copyMemberInfo(MemberInfo memberInfo) {
        accessFlags = memberInfo.getAccessFlag();
        name = memberInfo.getName();
        desc = memberInfo.getDesc();
    }

    public boolean canAccess(NiClass c) {
        if (isPublic()) {
            return true;
        }
        if (isProtected()) {
            return c == clz || clz.isSubClass(c) || clz.isSamePackge(c);
        }
        if (!isPrivate()) {
            return clz.isSamePackge(c);
        }
        return clz.isSame(c);
    }

    public boolean isSuper() {
        return (accessFlags & ClassConstant.ACC_SUPER) != 0;
    }

    public boolean isPublic() {
        return (accessFlags & ClassConstant.ACC_PUBLIC) != 0;
    }

    public boolean isProtected() {
        return (accessFlags & ClassConstant.ACC_PROTECTED) != 0;
    }

    public boolean isPrivate() {
        return (accessFlags & ClassConstant.ACC_PRIVATE) != 0;
    }

    public boolean isStatic() {
        return (accessFlags & ClassConstant.ACC_STATIC) != 0;
    }

    public boolean isFinal() {
        return (accessFlags & ClassConstant.ACC_FINAL) != 0;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public NiClass getClz() {
        return clz;
    }

    @Override
    public String toString() {
        return "ClassMember{" +
                "accessFlags=" + accessFlags +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", clz=" + clz +
                '}';
    }
}
