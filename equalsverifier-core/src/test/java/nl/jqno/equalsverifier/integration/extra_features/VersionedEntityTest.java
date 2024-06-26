package nl.jqno.equalsverifier.integration.extra_features;

import static nl.jqno.equalsverifier.internal.testhelpers.Util.defaultHashCode;

import java.util.Objects;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import nl.jqno.equalsverifier.internal.testhelpers.ExpectedException;
import nl.jqno.equalsverifier.testhelpers.annotations.javax.persistence.Id;
import org.junit.jupiter.api.Test;

// CHECKSTYLE OFF: LineLength

public class VersionedEntityTest {

    @Test
    public void fail_whenInstanceWithAZeroIdDoesNotEqualItself() {
        ExpectedException
            .when(() -> EqualsVerifier.forClass(OtherwiseStatelessVersionedEntity.class).verify())
            .assertFailure()
            .assertMessageContains(
                "object does not equal an identical copy of itself",
                Warning.IDENTICAL_COPY.toString()
            );
    }

    @Test
    public void fail_whenInstanceWithANonzeroIdEqualsItself_givenIdenticalCopyWarningIsSuppressed() {
        ExpectedException
            .when(() ->
                EqualsVerifier
                    .forClass(OtherwiseStatelessVersionedEntity.class)
                    .suppress(Warning.IDENTICAL_COPY)
                    .verify()
            )
            .assertFailure()
            .assertMessageContains("Unnecessary suppression", Warning.IDENTICAL_COPY.toString());
    }

    @Test
    public void succeed_whenInstanceWithAZeroIdDoesNotEqualItselfAndInstanceWithANonzeroIdDoes_givenVersionedEntityWarningIsSuppressed() {
        EqualsVerifier
            .forClass(OtherwiseStatelessVersionedEntity.class)
            .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY, Warning.SURROGATE_KEY)
            .verify();
    }

    @Test
    public void fail_whenInstanceWithAZeroIdDoesNotEqualItself_givenAVersionedEntityWithState() {
        ExpectedException
            .when(() -> EqualsVerifier.forClass(StringVersionedEntity.class).verify())
            .assertFailure()
            .assertMessageContains(
                "object does not equal an identical copy of itself",
                Warning.IDENTICAL_COPY.toString()
            );
    }

    @Test
    public void fail_whenInstanceWithANonzeroIdEqualsItself_givenAVersionedEntityWithStateAndIdenticalCopyWarningIsSuppressed() {
        ExpectedException
            .when(() ->
                EqualsVerifier
                    .forClass(StringVersionedEntity.class)
                    .suppress(Warning.IDENTICAL_COPY)
                    .verify()
            )
            .assertFailure()
            .assertMessageContains("Unnecessary suppression", Warning.IDENTICAL_COPY.toString());
    }

    @Test
    public void succeed_whenInstanceWithAZeroIdDoesNotEqualItselfAndInstanceWithANonzeroIdDoes_givenAVersionedEntityWithStateAndVersionedEntityWarningIsSuppressed() {
        EqualsVerifier
            .forClass(StringVersionedEntity.class)
            .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY, Warning.SURROGATE_KEY)
            .verify();
    }

    @Test
    public void fail_whenInstanceWithAZeroIdCanEqualItselfAndInstanceWithANonzeroIdAlso_givenAVersionedEntityWithState() {
        ExpectedException
            .when(() -> EqualsVerifier.forClass(WeakStringVersionedEntity.class).verify())
            .assertFailure()
            .assertMessageContains("Significant fields");
    }

    @Test
    public void succeed_whenInstanceWithAZeroIdCanEqualItselfAndInstanceWithANonzeroIdAlso_givenAVersionedEntityWithStateAndVersionedEntityWarningIsSuppressed() {
        EqualsVerifier
            .forClass(WeakStringVersionedEntity.class)
            .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY, Warning.SURROGATE_KEY)
            .verify();
    }

    @Test
    public void succeed_whenInstanceWithAZeroIdCanEqualItselfAndInstanceWithANonzeroIdAlso_givenAVersionedEntityWithStateAndAllFieldsWarningIsSuppressed() {
        EqualsVerifier
            .forClass(WeakStringVersionedEntity.class)
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .verify();
    }

    @Test
    public void succeed_whenInstanceWithANullableIdDoesNullCheck_givenAVersionedEntityWithStateAndVersionedEntityWarningIsSuppressed() {
        EqualsVerifier
            .forClass(NullCheckStringVersionedEntity.class)
            .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY, Warning.SURROGATE_KEY)
            .verify();
    }

    @Test
    public void succeed_whenEntityWithABusinessKey_givenAVersionedEntityWithStateAndVersionedEntityWarningIsSuppressed() {
        EqualsVerifier
            .forClass(BusinessKeyStringVersionedEntity.class)
            .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)
            .verify();
    }

    @Test
    public void succeed_whenTheParentOfTheVersionedEntityIsCheckedForSanity() {
        EqualsVerifier.forClass(CanEqualVersionedEntity.class).verify();
    }

    @Test
    public void fail_whenAnExceptionIsThrownInADifficultToReachPartOfTheSubclassOfAVersionedEntity() {
        ExpectedException
            .when(() -> EqualsVerifier.forClass(NonReflexiveCanEqualVersionedEntity.class).verify())
            .assertFailure()
            .assertMessageContains("catch me if you can");
    }

    @Test
    public void fail_whenTheExceptionIsThrownInADifficultToReachPartOfTheSubclassOfAVersionedEntity_givenVersionedEntityWarningIsSuppressed() {
        ExpectedException
            .when(() ->
                EqualsVerifier
                    .forClass(NonReflexiveCanEqualVersionedEntity.class)
                    .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)
                    .verify()
            )
            .assertFailure()
            .assertMessageContains("catch me if you can");
    }

    public static final class OtherwiseStatelessVersionedEntity {

        @Id
        private final long id;

        public OtherwiseStatelessVersionedEntity(long id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof OtherwiseStatelessVersionedEntity)) {
                return false;
            }
            OtherwiseStatelessVersionedEntity other = (OtherwiseStatelessVersionedEntity) obj;
            if (id == 0L && other.id == 0L) {
                return super.equals(obj);
            }
            return id == other.id;
        }

        @Override
        public int hashCode() {
            return defaultHashCode(this);
        }
    }

    public static final class StringVersionedEntity {

        @Id
        private final long id;

        @SuppressWarnings("unused")
        private final String s;

        public StringVersionedEntity(long id, String s) {
            this.id = id;
            this.s = s;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof StringVersionedEntity)) {
                return false;
            }
            StringVersionedEntity other = (StringVersionedEntity) obj;
            if (id == 0L && other.id == 0L) {
                return false;
            }
            return id == other.id;
        }

        @Override
        public int hashCode() {
            return Float.floatToIntBits(id);
        }
    }

    public static final class WeakStringVersionedEntity {

        @Id
        private final long id;

        private final String s;

        public WeakStringVersionedEntity(long id, String s) {
            this.id = id;
            this.s = s;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof WeakStringVersionedEntity)) {
                return false;
            }
            WeakStringVersionedEntity other = (WeakStringVersionedEntity) obj;
            if (id == 0L && other.id == 0L) {
                return Objects.equals(s, other.s);
            }
            return id == other.id;
        }

        @Override
        public int hashCode() {
            return Float.floatToIntBits(id);
        }
    }

    public static final class NullCheckStringVersionedEntity {

        @Id
        private final Long id;

        @SuppressWarnings("unused")
        private final String s;

        public NullCheckStringVersionedEntity(long id, String s) {
            this.id = id;
            this.s = s;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof NullCheckStringVersionedEntity)) {
                return false;
            }
            NullCheckStringVersionedEntity other = (NullCheckStringVersionedEntity) obj;
            return id != null && id.equals(other.id);
        }

        @Override
        public int hashCode() {
            return id == null ? 0 : Float.floatToIntBits(id);
        }
    }

    public static final class BusinessKeyStringVersionedEntity {

        @Id
        private final Long id;

        private final String s;

        public BusinessKeyStringVersionedEntity(long id, String s) {
            this.id = id;
            this.s = s;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BusinessKeyStringVersionedEntity)) {
                return false;
            }
            BusinessKeyStringVersionedEntity other = (BusinessKeyStringVersionedEntity) obj;
            return Objects.equals(s, other.s);
        }

        @Override
        public int hashCode() {
            return Objects.hash(s);
        }
    }

    private static class CanEqualVersionedEntity {

        private final Long id;

        public CanEqualVersionedEntity(Long id) {
            this.id = id;
        }

        @Override
        public final boolean equals(Object obj) {
            if (!(obj instanceof CanEqualVersionedEntity)) {
                return false;
            }
            CanEqualVersionedEntity other = (CanEqualVersionedEntity) obj;
            if (id != null) {
                return id.equals(other.id);
            } else if (other.id == null) {
                return other.canEqual(this);
            }
            return false;
        }

        public boolean canEqual(Object obj) {
            return obj instanceof CanEqualVersionedEntity;
        }

        @Override
        public final int hashCode() {
            return defaultHashCode(this);
        }
    }

    private static class NonReflexiveCanEqualVersionedEntity extends CanEqualVersionedEntity {

        public NonReflexiveCanEqualVersionedEntity(Long id) {
            super(id);
        }

        @Override
        public boolean canEqual(Object obj) {
            throw new IllegalStateException("catch me if you can");
        }
    }
}
