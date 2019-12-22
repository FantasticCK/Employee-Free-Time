package com.company;

public class Main {

    public static void main(String[] args) {
        // write your code here
    }
}

/*
// Definition for an Interval.
class Interval {
    public int start;
    public int end;

    public Interval() {}

    public Interval(int _start, int _end) {
        start = _start;
        end = _end;
    }
};
*/

// MergeSort
class Solution {
    public List<Interval> employeeFreeTime(List<List<Interval>> schedule) {
        List<Interval> res = new ArrayList<>();
        int n = schedule.size();
        List<Interval> workTimes = mergeIntervals(schedule, 0, n - 1);
        if (workTimes.size() < 2) {
            return res;
        }
        for (int i = 1; i < workTimes.size(); i++) {

            Interval temp = new Interval(workTimes.get(i - 1).end, workTimes.get(i).start);
            res.add(temp);
        }
        return res;
    }

    private List<Interval> mergeIntervals(List<List<Interval>> intervals, int st, int ed) {
        if (st > ed) {
            return new ArrayList<>();
        }

        if (st == ed) {
            return intervals.get(st);
        }

        int mid = st + (ed - st) / 2;
        return mergeTwoList(mergeIntervals(intervals, st, mid), mergeIntervals(intervals, mid + 1, ed));
    }

    private List<Interval> mergeTwoList(List<Interval> e1, List<Interval> e2) {

        if (e1.size() == 0) {
            return e2;
        }
        if (e2.size() == 0) {
            return e1;
        }

        List<Interval> res = new ArrayList<>();
        Interval temp = new Interval(-1, -1), curr;
        int i1 = 0, i2 = 0, len1 = e1.size(), len2 = e2.size();
        while (i1 < len1 && i2 < len2) {

            if (e1.get(i1).start <= e2.get(i2).start) {
                curr = e1.get(i1++);
            } else {
                curr = e2.get(i2++);
            }

            if (temp.start == -1) {
                temp = curr;
                continue;
            }

            if (curr.start <= temp.end) {
                temp = new Interval(Math.min(temp.start, curr.start), Math.max(curr.end, temp.end));
            } else {
                res.add(temp);
                temp = curr;
            }
        }

        while (i1 < len1) {
            curr = e1.get(i1++);
            if (curr.start <= temp.end) {
                temp = new Interval(Math.min(temp.start, curr.start), Math.max(curr.end, temp.end));
            } else {
                res.add(temp);
                temp = curr;
            }
        }

        while (i2 < len2) {
            curr = e2.get(i2++);
            if (curr.start <= temp.end) {
                temp = new Interval(Math.min(temp.start, curr.start), Math.max(curr.end, temp.end));
            } else {
                res.add(temp);
                temp = curr;
            }
        }
        res.add(temp);
        return res;
    }

}

// PQ
class Solution {
    public List<Interval> employeeFreeTime(List<List<Interval>> avails) {
        List<Interval> ans = new ArrayList();
        PriorityQueue<Job> pq = new PriorityQueue<Job>((a, b) ->
                avails.get(a.eid).get(a.index).start -
                        avails.get(b.eid).get(b.index).start);
        int ei = 0, anchor = Integer.MAX_VALUE;

        for (List<Interval> employee: avails) {
            pq.offer(new Job(ei++, 0));
            anchor = Math.min(anchor, employee.get(0).start);
        }

        while (!pq.isEmpty()) {
            Job job = pq.poll();
            Interval iv = avails.get(job.eid).get(job.index);
            if (anchor < iv.start)
                ans.add(new Interval(anchor, iv.start));
            anchor = Math.max(anchor, iv.end);
            if (++job.index < avails.get(job.eid).size())
                pq.offer(job);
        }

        return ans;
    }
}

class Job {
    int eid, index;
    Job(int e, int i) {
        eid = e;
        index = i;
    }
}