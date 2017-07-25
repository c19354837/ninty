package com.ninty.runtime.heap;

import com.ninty.classfile.MemberInfo;

/**
 * Created by ninty on 2017/7/23.
 */
public class ClassMember {

    private int accessFlags;
    private String name;
    private String desc;
    protected NiClass clz;

    void copy(MemberInfo memberInfo) {
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
        return c == clz;
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

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public NiClass getClz() {
        return clz;
    }
}
