package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.SortCommand;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * A person is considered unique by comparing using {@code Person#isSamePerson(Person)}. As such, adding and updating of
 * persons uses Person#isSamePerson(Person) for equality so as to ensure that the person being added or updated is
 * unique in terms of identity in the UniquePersonList. However, the removal of a person uses Person#equals(Object) so
 * as to ensure that the person with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Person#isSamePerson(Person)
 */
public class UniquePersonList implements Iterable<Person> {
    public static final String UNMODIFIABLE_MESSAGE = "Person List cannot be modified in this window, "
        + "please go back to Person Page.";
    private final ObservableList<Person> internalList = FXCollections.observableArrayList();
    private final ObservableList<Person> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    private boolean isChangeable = true;

    /**
     * set if the list can be changed
     */
    public void setChangeable(boolean change) {
        isChangeable = change;
    }

    public boolean getChangeable() {
        return this.isChangeable;
    }

    /**
     * refreshes the list
     */
    public void refreshList() {
        if (!internalList.isEmpty()) {
            Person person = internalList.get(0);
            internalList.remove(0);
            internalList.add(0, person);
        }
    }

    /**
     * filters the list
     */
    public void filter(Integer index, LoanPredicate pred) {
        if (index == -2) { // special index for filter all
            for (Person person : internalList) {
                LoanList loanList = person.getLoanList();
                loanList.filter(pred);
            }
        } else { // filter specific person
            LoanList loanList = internalList.get(index).getLoanList();
            loanList.filter(pred);
        }
        refreshList();
    }

    /**
     * Sorts the list
     */
    public void sort(String sort, String order) {
        Comparator<Person> comparator;

        if (sort.equals(SortCommand.NAME)) {
            comparator = Comparator.comparing(
                person -> person.getName().toString()
            );
        } else if (sort.equals(SortCommand.OVERDUE)) {
            comparator = Comparator.comparing(
                person -> person.getLoanList().getMostOverdueMonths()
            );
        } else if (sort.equals(SortCommand.AMOUNT)) {
            comparator = Comparator.comparing(
                person -> person.getLoanList().getTotalLoanOwed()
            );
        } else { // default case, should never arrive here, parser will handle
            comparator = Comparator.comparing(
                person -> person.getName().toString()
            );
        }

        if (order.equals(SortCommand.DESC)) {
            comparator = comparator.reversed();
        }

        FXCollections.sort(internalList, comparator);
    }

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(Person toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Adds a person to the list.
     * The person must not already exist in the list.
     */
    public void add(Person toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the list.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.isSamePerson(editedPerson) && contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedPerson);
    }

    /**
     * Removes the equivalent person from the list.
     * The person must exist in the list.
     */
    public void remove(Person toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PersonNotFoundException();
        }
    }

    public void setPersons(UniquePersonList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        requireAllNonNull(persons);
        if (!personsAreUnique(persons)) {
            throw new DuplicatePersonException();
        }

        internalList.setAll(persons);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Person> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniquePersonList)) {
            return false;
        }

        UniquePersonList otherUniquePersonList = (UniquePersonList) other;
        return internalList.equals(otherUniquePersonList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code persons} contains only unique persons.
     */
    private boolean personsAreUnique(List<Person> persons) {
        for (int i = 0; i < persons.size() - 1; i++) {
            for (int j = i + 1; j < persons.size(); j++) {
                if (persons.get(i).isSamePerson(persons.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
