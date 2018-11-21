/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.mail.search;

import java.util.Date;

/**
 * This class implements comparisons for Dates
 *
 * @author Bill Shannon
 * @author John Mani
 */
public abstract class DateTerm extends ComparisonTerm {
    /**
     * The date.
     *
     * @serial
     */
    protected Date date;

    private static final long serialVersionUID = 4818873430063720043L;

    /**
     * Constructor.
     *
     * @param comparison the comparison type
     * @param date The Date to be compared against
     */
    protected DateTerm(int comparison, Date date) {
        this.comparison = comparison;
        this.date = date;
    }

    /**
     * Return the Date to compare with.
     *
     * @return the date
     */
    public Date getDate() {
        return new Date(date.getTime());
    }

    /**
     * Return the type of comparison.
     *
     * @return the comparison type
     */
    public int getComparison() {
        return comparison;
    }

    /**
     * The date comparison method.
     *
     * @param d the date in the constructor is compared with this date
     * @return true if the dates match, otherwise false
     */
    protected boolean match(Date d) {
        switch (comparison) {
            case LE:
                return d.before(date) || d.equals(date);
            case LT:
                return d.before(date);
            case EQ:
                return d.equals(date);
            case NE:
                return !d.equals(date);
            case GT:
                return d.after(date);
            case GE:
                return d.after(date) || d.equals(date);
            default:
                return false;
        }
    }

    /**
     * Equality comparison.
     */
    @Override public boolean equals(Object obj) {
        if (!(obj instanceof DateTerm)) return false;
        DateTerm dt = (DateTerm) obj;
        return dt.date.equals(this.date) && super.equals(obj);
    }

    /**
     * Compute a hashCode for this object.
     */
    @Override public int hashCode() {
        return date.hashCode() + super.hashCode();
    }
}
