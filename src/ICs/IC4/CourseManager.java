/**Class: CourseManager
 * @author Hugo Padilla
 * @version 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: September 25th, 2024
 *
 * This class – This is the CourseManager, which stores the courses and their associated students.
 * The class uses a TreeMap to maintain the courses in alphabetical order and an ArrayList to store
 * the students for each course. It provides methods to:
 * - Add a course to the map if the course is unique. (addCourse)
 * - Add students to a specific course. (addStudentToCourse)
 * - Retrieve the number of students in each course. (getStudentsCount)
 * - Retrieve the total number of courses. (getCourseCount)
 * - Retrieve an iterator over the courses. (getCourseIterator)
 * The class is designed to be used in conjunction with the CourseManagerMain class,
 * where it handles the back-end logic for storing and retrieving course and student data.
 */
package ICs.IC4;
import java.util.*;

public class CourseManager {
    // Map to store courses and their students
    private final Map<String, ArrayList<String>> courseMap;
    private final Set<String> courseSet;

    public CourseManager() {
        courseMap = new TreeMap<>(); // TreeMap for alphabetical order
        courseSet = new HashSet<>(); // Set to track unique course names
    }

    // Add a new course to the map
    public boolean addCourse(String courseName) {
        if (courseSet.add(courseName)) {
            courseMap.put(courseName, new ArrayList<>()); // Add new course if unique
            return true;
        }
        return false; // Course already exists
    }

    // Add a student to a specific course
    public void addStudentToCourse(String courseName, String studentName) {
        ArrayList<String> students = courseMap.get(courseName);
        students.add(studentName); // Allow duplicate student names
    }

    // Get the course map for displaying roles
    public Map<String, ArrayList<String>> getCourseMap() {
        return courseMap;
    }

    // Get an iterator over the sorted course names
    public Iterator<String> getCourseIterator() {
        return courseMap.keySet().iterator();
    }

    // Get the count of courses added
    public int getCourseCount() {
        return courseMap.size();
    }
    // Get the number of students in a specific course
    public int getStudentsCount(String courseName) {
        ArrayList<String> students = courseMap.get(courseName);
        return students.size();
    }
}
