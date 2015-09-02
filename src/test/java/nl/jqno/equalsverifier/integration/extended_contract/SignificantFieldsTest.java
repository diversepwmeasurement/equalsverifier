/*
 * Copyright 2009-2010, 2012-2015 Jan Ouwens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.jqno.equalsverifier.integration.extended_contract;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.testhelpers.IntegrationTestBase;
import nl.jqno.equalsverifier.testhelpers.types.Color;
import nl.jqno.equalsverifier.testhelpers.types.FinalPoint;
import nl.jqno.equalsverifier.testhelpers.types.Point;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Objects;

import static nl.jqno.equalsverifier.testhelpers.Util.defaultHashCode;

public class SignificantFieldsTest extends IntegrationTestBase {
    @Test
    public void fail_whenEqualsUsesAFieldAndHashCodeDoesnt() {
        expectFailure("Significant fields", "equals relies on", "yNotUsed", "but hashCode does not");
        EqualsVerifier.forClass(ExtraFieldInEquals.class)
                .verify();
    }

    @Test
    public void fail_whenHashCodeUsesAFieldAndEqualsDoesnt() {
        expectFailure("Significant fields", "hashCode relies on", "yNotUsed", "but equals does not");
        EqualsVerifier.forClass(ExtraFieldInHashCode.class)
                .verify();
    }

    @Test
    public void succeed_whenAllFieldsAreUsed_givenAllFieldsShouldBeUsed() {
        EqualsVerifier.forClass(FinalPoint.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test
    public void succeed_whenAFieldIsUnused_givenAllFieldsWarningIsSuppressed() {
        EqualsVerifier.forClass(OneFieldUnused.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test
    public void fail_whenAFieldIsUnused() {
        expectFailure("Significant fields", "equals does not use", "colorNotUsed");
        EqualsVerifier.forClass(OneFieldUnused.class)
                .verify();
    }

    @Test
    public void succeed_whenATransientFieldIsUnused_givenAllFieldsShouldBeUsed() {
        EqualsVerifier.forClass(OneTransientFieldUnusedColorPoint.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test
    public void succeed_whenAStaticFieldIsUnused_givenAllFieldsShouldBeUsed() {
        EqualsVerifier.forClass(OneStaticFieldUnusedColorPoint.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test
    public void succeed_whenAFieldIsUnusedInASubclass_givenAllFieldsWarningIsSuppressed() {
        EqualsVerifier.forClass(OneFieldUnusedExtended.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test
    public void fail_whenAFieldIsUnusedInASubclass() {
        expectFailure("Significant fields", "equals does not use", "colorNotUsed");
        EqualsVerifier.forClass(OneFieldUnusedExtended.class)
                .verify();
    }

    @Test
    public void succeed_whenNoEqualsMethodPresent_givenAllFieldsWarningIsSuppressed() {
        EqualsVerifier.forClass(NoFieldsUsed.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test
    public void succeed_whenNoFieldsAreAdded_givenAllFieldsShouldBeUsed() {
        EqualsVerifier.forClass(NoFieldsAdded.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test
    public void fail_whenNoFieldsAreUsed() {
        expectFailure("Significant fields", "equals does not use", "color");
        EqualsVerifier.forClass(NoFieldsUsed.class)
                .verify();
    }

    @Test
    public void fail_whenNoFieldsAreUsed_givenUsingGetClass() {
        expectFailure("Significant fields", "equals does not use", "color");
        EqualsVerifier.forClass(NoFieldsUsed.class)
                .usingGetClass()
                .verify();
    }

    @Test
    public void succeed_whenAFieldIsUnused_givenAllFieldsShouldBeUsedExceptThatField() {
        EqualsVerifier.forClass(OneFieldUnused.class)
                .allFieldsShouldBeUsedExcept("colorNotUsed")
                .verify();
    }

    @Test
    public void succeed_whenTwoFieldsAreUnused_givenAllFieldsShouldBeUsedExceptThoseTwo() {
        EqualsVerifier.forClass(TwoFieldsUnusedColorPoint.class)
                .allFieldsShouldBeUsedExcept("colorNotUsed", "colorAlsoNotUsed")
                .verify();
    }

    @Test
    public void fail_whenTwoFieldsAreUnUsed_givenAllFieldsShouldBeUsedExceptOneOfThemButNotBoth() {
        expectFailure("Significant fields", "equals does not use", "colorAlsoNotUsed");
        EqualsVerifier.forClass(TwoFieldsUnusedColorPoint.class)
                .allFieldsShouldBeUsedExcept("colorNotUsed")
                .verify();
    }

    @Test
    public void fail_whenAllFieldsAreUsed_givenAllFieldsShouldBeUsedExceptOneThatActuallyIsUsed() {
        expectFailure("Significant fields", "equals should not use", "x", "but it does");
        EqualsVerifier.forClass(FinalPoint.class)
                .allFieldsShouldBeUsedExcept("x")
                .verify();
    }

    @Test
    public void fail_whenOneFieldIsUnused_givenAllFieldsShouldBeUsedExceptTwoFields() {
        expectFailure("Significant fields", "equals should not use", "x", "but it does");
        EqualsVerifier.forClass(OneFieldUnused.class)
                .allFieldsShouldBeUsedExcept("x", "colorNotUsed")
                .verify();
    }

    @Test
    public void anExceptionIsThrown_whenANonExistingFieldIsExcepted() {
        expectException(IllegalArgumentException.class, "Class FinalPoint does not contain field thisFieldDoesNotExist.");
        EqualsVerifier.forClass(FinalPoint.class)
                .allFieldsShouldBeUsedExcept("thisFieldDoesNotExist");
    }

    @Test
    public void succeed_whenAUsedFieldHasUnusedStaticFinalMembers_givenAllFieldsarningIsSuppressed() {
        EqualsVerifier.forClass(IndirectStaticFinalContainer.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test@Ignore("TODO: how should this interact with allFieldsShouldBeUsed?")
    public void succeed_whenUnusedFieldIsStateless() {
        EqualsVerifier.forClass(UnusedStatelessContainer.class)
                .verify();
    }

    @Test@Ignore("TODO: how should this interact with allFieldsShouldBeUsed?")
    public void succeed_whenUsedFieldIsStateless() {
        EqualsVerifier.forClass(UsedStatelessContainer.class)
                .verify();
    }

    @Test
    public void succeed_whenClassIsStateless_givenAllFieldsWarningIsSuppressed() {
        EqualsVerifier.forClass(Stateless.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test
    public void fail_whenNonNullFieldIsEqualToNullField() {
        expectFailure("Significant fields", "hashCode relies on", "s", "equals does not", "These objects are equal, but probably shouldn't be");
        EqualsVerifier.forClass(BugWhenFieldIsNull.class)
                .verify();
    }

    static final class ExtraFieldInEquals {
        private final int x;
        private final int yNotUsed;

        public ExtraFieldInEquals(int x, int y) { this.x = x; this.yNotUsed = y; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ExtraFieldInEquals)) {
                return false;
            }
            ExtraFieldInEquals p = (ExtraFieldInEquals)obj;
            return p.x == x && p.yNotUsed == yNotUsed;
        }

        @Override
        public int hashCode() {
            return x;
        }
    }

    static final class ExtraFieldInHashCode {
        private final int x;
        private final int yNotUsed;

        public ExtraFieldInHashCode(int x, int y) { this.x = x; this.yNotUsed = y; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ExtraFieldInHashCode)) {
                return false;
            }
            ExtraFieldInHashCode p = (ExtraFieldInHashCode)obj;
            return p.x == x;
        }

        @Override
        public int hashCode() {
            return x + (31 * yNotUsed);
        }
    }

    static final class OneFieldUnused {
        private final int x;
        private final int y;
        @SuppressWarnings("unused")
        private final Color colorNotUsed;

        public OneFieldUnused(int x, int y, Color color) { this.x = x; this.y = y; this.colorNotUsed = color; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof OneFieldUnused)) {
                return false;
            }
            OneFieldUnused other = (OneFieldUnused)obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return x + (31 * y);
        }
    }

    static final class OneTransientFieldUnusedColorPoint {
        private final int x;
        private final int y;
        @SuppressWarnings("unused")
        private transient final Color color;

        public OneTransientFieldUnusedColorPoint(int x, int y, Color color) { this.x = x; this.y = y; this.color = color; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof OneTransientFieldUnusedColorPoint)) {
                return false;
            }
            OneTransientFieldUnusedColorPoint other = (OneTransientFieldUnusedColorPoint)obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return x + (31 * y);
        }
    }

    static final class OneStaticFieldUnusedColorPoint {
        private final int x;
        private final int y;
        @SuppressWarnings("unused")
        private static Color color;

        public OneStaticFieldUnusedColorPoint(int x, int y, Color color) { this.x = x; this.y = y; OneStaticFieldUnusedColorPoint.color = color; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof OneStaticFieldUnusedColorPoint)) {
                return false;
            }
            OneStaticFieldUnusedColorPoint other = (OneStaticFieldUnusedColorPoint)obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return x + (31 * y);
        }
    }

    static final class OneFieldUnusedExtended extends Point {
        @SuppressWarnings("unused")
        private final Color colorNotUsed;

        public OneFieldUnusedExtended(int x, int y, Color color) { super(x, y); this.colorNotUsed = color; }
    }

    static final class NoFieldsUsed {
        @SuppressWarnings("unused")
        private final Color color;

        public NoFieldsUsed(Color color) { this.color = color; }
    }

    static final class NoFieldsAdded extends Point {
        public NoFieldsAdded(int x, int y) {
            super(x, y);
        }
    }

    static final class TwoFieldsUnusedColorPoint {
        private final int x;
        private final int y;
        @SuppressWarnings("unused")
        private final Color colorNotUsed;
        @SuppressWarnings("unused")
        private final Color colorAlsoNotUsed;

        public TwoFieldsUnusedColorPoint(int x, int y, Color color)
            { this.x = x; this.y = y; this.colorNotUsed = color; this.colorAlsoNotUsed = color; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TwoFieldsUnusedColorPoint)) {
                return false;
            }
            TwoFieldsUnusedColorPoint other = (TwoFieldsUnusedColorPoint)obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return x + (31 * y);
        }
    }

    static final class X {
        public static final X x = new X();

        @Override public boolean equals(Object obj) { return obj instanceof X; }
        @Override public int hashCode() { return 42; }
    }

    static final class IndirectStaticFinalContainer {
        private final X x;

        public IndirectStaticFinalContainer(X x) { this.x = x; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof IndirectStaticFinalContainer)) {
                return false;
            }
            return Objects.equals(x, ((IndirectStaticFinalContainer)obj).x);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(x);
        }
    }

    static final class Stateless {
        @SuppressWarnings("unused")
        private final int irrelevant;

        public Stateless(int i) { this.irrelevant = i; }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Stateless;
        }

        @Override
        public int hashCode() {
            return 42;
        }
    }

    static final class UnusedStatelessContainer {
        private final int i;
        @SuppressWarnings("unused")
        private final Stateless stateless;

        public UnusedStatelessContainer(int i, Stateless stateless) { this.i = i; this.stateless = stateless; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof UnusedStatelessContainer)) {
                return false;
            }
            UnusedStatelessContainer other = (UnusedStatelessContainer)obj;
            return i == other.i;
        }

        @Override
        public int hashCode() {
            return i;
        }
    }

    static final class UsedStatelessContainer {
        private final int i;
        private final Stateless stateless;

        public UsedStatelessContainer(int i, Stateless stateless) { this.i = i; this.stateless = stateless; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof UsedStatelessContainer)) {
                return false;
            }
            UsedStatelessContainer other = (UsedStatelessContainer)obj;
            return i == other.i && Objects.equals(stateless, other.stateless);
        }

        @Override public int hashCode() { return defaultHashCode(this); }
    }

    public static final class BugWhenFieldIsNull {
        private final String s;

        public BugWhenFieldIsNull(String s) { this.s = s; }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BugWhenFieldIsNull)) {
                return false;
            }
            BugWhenFieldIsNull other = (BugWhenFieldIsNull)obj;
            return !(this.s != null && other.s != null) || this.s.equals(other.s);
        }

        @Override public int hashCode() { return Objects.hashCode(s); }
    }
}
