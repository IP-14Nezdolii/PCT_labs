package com.example.journal;

import java.util.ArrayList;
import java.util.List;

public class Journal {
    private List<Group> groupsList = new ArrayList<>();

    public Journal(int nGroups, int nGroupStudents) {
        for (int i = 0; i < nGroups; i++) 
            groupsList.add(new Group(nGroupStudents));
    }

    public int groupNumb() {
        return groupsList.size();
    }

    public int studentsNumb() {
        int numb = 0;
        for (int i = 0; i < groupsList.size(); i++) {
            numb += groupsList.get(i).size();
        }

        return numb;
    }

    public Group getGroup(int group) {
        return groupsList.get(group);
    }

    public void outJournal() {
        for (int i = 0; i < groupsList.size(); i++) {
            System.out.println("Group " + i);
            for (int j = 0; j < groupsList.get(i).size(); j++) {
                System.out.println("Student " + j + ": " + groupsList.get(i).get(j));
            }
            System.out.println();
        }
    }
}

class Group {
    private List<List<Integer>> group = new ArrayList<>();

    public Group(int nStudents) {
        for (int i = 0; i < nStudents; i++) {
            group.add(new ArrayList<>());
        }
    }

    public int size() {
        return group.size();
    }

    public List<Integer> get(int student) {
        return group.get(student);
    }

    public List<Integer> getStudentMarks(int student) {
        return group.get(student);
    }
}
