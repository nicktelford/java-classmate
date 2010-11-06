package com.fasterxml.classmate.types;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeBindings;

/**
 * Type implementation for classes that do not represent interfaces,
 * primitive or array types.
 */
public class ResolvedClass extends ResolvedType
{
    protected final ResolvedClass _superClass;
    /**
     * List of interfaces this type implements; may be empty but never null
     */
    protected final ResolvedType[] _superInterfaces;

    protected final int _modifiers;
    
    /*
    /**********************************************************************
    /* Life cycle
    /**********************************************************************
     */
    
    public ResolvedClass(Class<?> erased, TypeBindings bindings,
            ResolvedClass superClass, ResolvedType[] interfaces)
    {
        super(erased, bindings);
        _superClass = superClass;
        _superInterfaces = (interfaces == null) ? NO_TYPES : interfaces;
        _modifiers = erased.getModifiers();
    }

    /*
    /**********************************************************************
    /* Accessors for related types
    /**********************************************************************
     */

    @Override
    public ResolvedClass getParentClass() { return _superClass; }

    @Override
    public List<ResolvedType> getImplementedInterfaces() {
        return (_superInterfaces.length == 0) ?
                Collections.<ResolvedType>emptyList() : Arrays.asList(_superInterfaces);
    }

    /*
    /**********************************************************************
    /* Accessors for related types
    /**********************************************************************
     */
    
    @Override
    public final ResolvedType getArrayElementType() { return null; }
    
    /*
    /**********************************************************************
    /* Simple property accessors
    /**********************************************************************
     */
    
    @Override
    public final boolean isInterface() { return false; }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(_modifiers);
    }

    @Override
    public final boolean isArray() { return false; }

    @Override
    public final boolean isPrimitive() { return false; }
    
    /*
    /**********************************************************************
    /* String representations
    /**********************************************************************
     */

    @Override
    public StringBuilder appendSignature(StringBuilder sb) {
        return _appendClassSignature(sb);
    }

    @Override
    public StringBuilder appendErasedSignature(StringBuilder sb) {
        return _appendErasedClassSignature(sb);
    }

    @Override
    public StringBuilder appendBriefDescription(StringBuilder sb) {
        return _appendClassDescription(sb);
    }
    
    @Override
    public StringBuilder appendFullDescription(StringBuilder sb)
    {
        sb = _appendClassDescription(sb);
        if (_superClass != null) {
            sb.append(" extends ");
            sb = _superClass.appendBriefDescription(sb);
        }
        // interfaces 'extend' other interfaces...
        int count = _superInterfaces.length;
        if (count > 0) {
            sb.append(" implements ");
            for (int i = 0; i < count; ++i) {
                if (i > 0) {
                    sb.append(",");
                }
                sb = _superInterfaces[i].appendBriefDescription(sb);
            }
        }
        return sb;
    }
}
