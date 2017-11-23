/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.sun.tools.visualvm.heapviewer.truffle.python;

import com.sun.tools.visualvm.heapviewer.HeapContext;
import com.sun.tools.visualvm.heapviewer.HeapFragment;
import com.sun.tools.visualvm.heapviewer.truffle.TruffleLanguageHeapFragment;
import com.sun.tools.visualvm.heapviewer.truffle.TruffleLanguageSupport;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.Instance;
import org.netbeans.lib.profiler.heap.JavaClass;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Tomas Hurka
 */
public class PythonHeapFragment extends TruffleLanguageHeapFragment {

    private static final String PYTHON_HEAP_ID = "python_heap";


    PythonHeapFragment(Instance langInfo, Heap heap) throws IOException {
        super("Python Heap", PYTHON_HEAP_ID, langInfo, heap);
    }


    static boolean isPythonHeap(HeapContext context) {
        return PYTHON_HEAP_ID.equals(context.getFragment().getID()); // NOI18N
    }

    public static HeapContext getPythonContext(HeapContext context) {
        if (isPythonHeap(context)) return context;

        for (HeapContext otherContext : context.getOtherContexts())
            if (isPythonHeap(otherContext)) return otherContext;

        return null;
    }

    static PythonHeapFragment heap(Heap heap) {
        return (PythonHeapFragment)heap;
    }


    Iterator<Instance> getPythonObjectsIterator() {
        return getInstancesIterator(PythonObject.PYTHON_OBJECT_FQN);
    }


    @ServiceProvider(service=HeapFragment.Provider.class, position = 400)
    public static class Provider extends HeapFragment.Provider {

        private static final String PYTHON_LANG_ID_OLD = "python";  // NOI18N
        private static final String PYTHON_LANG_ID = "Python";  // NOI18N

        public HeapFragment getFragment(File heapDumpFile, Lookup.Provider heapDumpProject, Heap heap) throws IOException {
            Instance langInfo = TruffleLanguageSupport.getLanguageInfo(heap, PYTHON_LANG_ID);
            JavaClass PythonMainClass = heap.getJavaClassByName(PythonObject.PYTHON_OBJECT_FQN);

            if (langInfo == null) {
                langInfo = TruffleLanguageSupport.getLanguageInfo(heap, PYTHON_LANG_ID_OLD);
            }

            return langInfo != null && PythonMainClass != null ? new PythonHeapFragment(langInfo, heap) : null;
        }

    }

}
