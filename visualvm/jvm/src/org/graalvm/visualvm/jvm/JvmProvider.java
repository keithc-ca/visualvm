/*
 * Copyright (c) 2007, 2021, Oracle and/or its affiliates. All rights reserved.
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

package org.graalvm.visualvm.jvm;

import org.graalvm.visualvm.application.Application;
import org.graalvm.visualvm.application.jvm.Jvm;
import org.graalvm.visualvm.core.model.AbstractModelProvider;
import org.graalvm.visualvm.tools.jmx.JmxModel;
import org.graalvm.visualvm.tools.jmx.JmxModelFactory;
import org.graalvm.visualvm.tools.jmx.JvmMXBeans;
import org.graalvm.visualvm.tools.jvmstat.JvmstatModel;
import org.graalvm.visualvm.tools.jvmstat.JvmstatModelFactory;

/**
 *
 * @author Tomas Hurka
 */
class JvmProvider extends AbstractModelProvider<Jvm, Application> {

    public Jvm createModelFor(Application app) {
        JvmstatModel jvmstat = JvmstatModelFactory.getJvmstatFor(app);
        JVMImpl jvm = null;
        if (jvmstat != null) {
            jvm = new JVMImpl(app, jvmstat);
        } else {
            JmxModel jmxModel = JmxModelFactory.getJmxModelFor(app);
            if (jmxModel != null && jmxModel.getConnectionState() == JmxModel.ConnectionState.CONNECTED) {
                JvmMXBeans jmx = jmxModel.getJvmMXBeans();
                if (jmx != null && jmx.getRuntimeMXBean() != null) {
                    jvm = new JVMImpl(app);
                }
            }
        }
        return jvm;
    }
}
