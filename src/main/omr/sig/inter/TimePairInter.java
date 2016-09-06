//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                    T i m e P a i r I n t e r                                   //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//
//  Copyright © Hervé Bitteur and others 2000-2016. All rights reserved.
//
//  This program is free software: you can redistribute it and/or modify it under the terms of the
//  GNU Affero General Public License as published by the Free Software Foundation, either version
//  3 of the License, or (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//  See the GNU Affero General Public License for more details.
//
//  You should have received a copy of the GNU Affero General Public License along with this
//  program.  If not, see <http://www.gnu.org/licenses/>.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package omr.sig.inter;

import omr.score.TimeRational;

import omr.sheet.Staff;

import omr.util.Entities;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class {@code TimePairInter} is a time signature composed of two halves.
 *
 * @author Hervé Bitteur
 */
@XmlRootElement(name = "time-pair")
@XmlAccessorType(XmlAccessType.NONE)
public class TimePairInter
        extends AbstractTimeInter
        implements InterEnsemble
{
    //~ Instance fields ----------------------------------------------------------------------------

    // Persistent data
    //----------------
    //
    @XmlElement(name = "number")
    private List<TimeNumberInter> members;

    //~ Constructors -------------------------------------------------------------------------------
    /**
     * (Private) constructor.
     *
     * @param num
     * @param den
     * @param timeRational
     * @param grade
     */
    private TimePairInter (TimeNumberInter num,
                           TimeNumberInter den,
                           Rectangle bounds,
                           TimeRational timeRational,
                           double grade)
    {
        super(null, bounds, timeRational, grade);
        members = Arrays.asList(num, den);
    }

    /**
     * No-arg constructor meant for JAXB.
     */
    private TimePairInter ()
    {
        super(null, null, 0);
    }

    //~ Methods ------------------------------------------------------------------------------------
    //--------//
    // accept //
    //--------//
    @Override
    public void accept (InterVisitor visitor)
    {
        visitor.visit(this);
    }

    //--------//
    // create //
    //--------//
    /**
     * Create a {@code TimePairInter} object from its 2 numbers.
     *
     * @param num numerator
     * @param den denominator
     * @return the created instance
     */
    public static TimePairInter create (TimeNumberInter num,
                                        TimeNumberInter den)
    {
        Rectangle box = num.getBounds();
        box.add(den.getBounds());

        TimeRational timeRational = new TimeRational(num.getValue(), den.getValue());
        double grade = 0.5 * (num.getGrade() + den.getGrade());
        TimePairInter pair = new TimePairInter(num, den, box, timeRational, grade);

        ///pair.setContextualGrade(0.5 * (num.getContextualGrade() + den.getContextualGrade()));
        return pair;
    }

    //-----------//
    // getBounds //
    //-----------//
    @Override
    public Rectangle getBounds ()
    {
        if (bounds == null) {
            bounds = Entities.getBounds(getMembers());
        }

        return bounds;
    }

    /**
     * @return the den
     */
    public TimeNumberInter getDen ()
    {
        return (TimeNumberInter) members.get(1);
    }

    //------------//
    // getMembers //
    //------------//
    @Override
    public List<? extends Inter> getMembers ()
    {
        return members;
    }

    /**
     * @return the num
     */
    public TimeNumberInter getNum ()
    {
        return (TimeNumberInter) members.get(0);
    }

    //-----------------//
    // getSymbolBounds //
    //-----------------//
    /**
     * {@inheritDoc}.
     * <p>
     * This implementation uses two rectangles, one for numerator and one for denominator.
     *
     * @param interline scaling factor
     * @return the symbol bounds
     */
    @Override
    public Rectangle getSymbolBounds (int interline)
    {
        Rectangle rect = getNum().getSymbolBounds(interline);
        rect.add(getDen().getSymbolBounds(interline));

        return rect;
    }

    //-----------//
    // replicate //
    //-----------//
    @Override
    public TimePairInter replicate (Staff targetStaff)
    {
        TimePairInter inter = new TimePairInter(null, null, null, timeRational, 0);
        inter.setStaff(targetStaff);

        return inter;
    }

    //-------------//
    // shapeString //
    //-------------//
    @Override
    public String shapeString ()
    {
        return "TIME_SIG_" + timeRational;
    }
}
