/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
 */
package org.graalvm.visualvm.lib.jfluid.results.locks;

import org.graalvm.visualvm.lib.jfluid.results.AbstractDataFrameProcessor;
import org.graalvm.visualvm.lib.jfluid.results.ProfilingResultListener;

/**
 *
 * @author Tomas Hurka
 */
public abstract class AbstractLockDataFrameProcessor extends AbstractDataFrameProcessor {
    
    protected volatile int currentThreadId = -1;

    protected void fireMonitorEntry(final int threadId, final long timeStamp0, final long timeStamp1, final int monitorId, final int ownerThreadId) {
        foreachListener(new ListenerFunctor() {
            public void execute(ProfilingResultListener listener) {
                ((LockProfilingResultListener) listener).monitorEntry(threadId, timeStamp0, timeStamp1, monitorId, ownerThreadId);
            }
        });
    }

    protected void fireMonitorExit(final int threadId, final long timeStamp0, final long timeStamp1, final int monitorId) {
        foreachListener(new ListenerFunctor() {
            public void execute(ProfilingResultListener listener) {
                ((LockProfilingResultListener) listener).monitorExit(threadId, timeStamp0, timeStamp1, monitorId);
            }
        });
    }

    protected void fireNewMonitor(final int hash, final String className) {
        foreachListener(new ListenerFunctor() {
            public void execute(ProfilingResultListener listener) {
                ((LockProfilingResultListener) listener).newMonitor(hash, className);
            }
        });
    }

    protected void fireNewThread(final int threadId, final String threadName, final String threadClassName) {
        foreachListener(new ListenerFunctor() {
            public void execute(ProfilingResultListener listener) {
                ((LockProfilingResultListener) listener).newThread(threadId, threadName, threadClassName);
            }
        });
    }

    protected void fireAdjustTime(final int threadId, final long timeStamp0, final long timeStamp1) {
        foreachListener(new ListenerFunctor() {
                public void execute(ProfilingResultListener listener) {
                    ((LockProfilingResultListener) listener).timeAdjust(threadId, timeStamp0, timeStamp1);
                }
            });
    }
}
