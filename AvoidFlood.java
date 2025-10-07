/*
Your country has an infinite number of lakes. Initially, all the lakes are empty, but when it rains over the nth lake, the nth lake becomes full of water. If it rains over a lake that is full of water, there will be a flood. Your goal is to avoid floods in any lake.

Given an integer array rains where:

rains[i] > 0 means there will be rains over the rains[i] lake.
rains[i] == 0 means there are no rains this day and you can choose one lake this day and dry it.
Return an array ans where:

ans.length == rains.length
ans[i] == -1 if rains[i] > 0.
ans[i] is the lake you choose to dry in the ith day if rains[i] == 0.
If there are multiple valid answers return any of them. If it is impossible to avoid flood return an empty array.

Notice that if you chose to dry a full lake, it becomes empty, but if you chose to dry an empty lake, nothing changes.
  */








// this also works
// st keeps indexes of dry days (0s) — sorted automatically.
// Why TreeSet?
// Because we’ll need to find the nearest dry day after a certain rain day, and TreeSet supports ceiling() (find smallest element ≥ given value) efficiently.

// class Solution {

//     public int[] avoidFlood(int[] rains) {
//         int[] ans = new int[rains.length];
//         Arrays.fill(ans, 1);
//         TreeSet<Integer> st = new TreeSet<Integer>();
//         Map<Integer, Integer> mp = new HashMap<Integer, Integer>();
//         for (int i = 0; i < rains.length; ++i) {
//             if (rains[i] == 0) {
//                 st.add(i);
//             } else {
//                 ans[i] = -1;
//                 if (mp.containsKey(rains[i])) {
//                     Integer it = st.ceiling(mp.get(rains[i]));
//                     if (it == null) {
//                         return new int[0];
//                     }
//                     ans[it] = rains[i];
//                     st.remove(it);
//                 }
//                 mp.put(rains[i], i);
//             }
//         }
//         return ans;
//     }
// }

class Solution {
    public int[] avoidFlood(int[] rain) {
        int n = rain.length;
        UnionFind uf = new UnionFind(n + 1);
        Map<Integer, Integer> map = new HashMap<>();
        int[] res = new int[n];
        Arrays.fill(res, 1);

        for (int i = 0; i < n; i++) {
            int lake = rain[i];
            // we assume that in the uf, if value=index, it is a dry day,
            // so lake==0 is of no use to us
            if (lake == 0) continue;

            res[i] = -1;
            uf.findNextDryDayAndAssignAsParentOf(i);

            if (map.containsKey(lake)) {
                int prev = map.get(lake);
                int dry = uf.findADryDayStartingFrom(prev + 1);
                // if the next available day to dry is beyond i, no use then,
                // because 'i' is where the lake is getting filled again.
                if (dry >= i) return new int[0];

                res[dry] = lake;
                uf.findNextDryDayAndAssignAsParentOf(dry);
                map.put(lake, i);
            } else {
                map.put(lake, i);
            }
        }

        return res;
    }
}

class UnionFind {
    int[] parent;

    public UnionFind(int size) {
        parent = new int[size + 1];
        // if value=index, it means day is dry
        for (int i = 0; i <= size; i++) {
            parent[i] = i;
        }
    }

    public int findADryDayStartingFrom(int i) {
        if (parent[i] == i)
            return i;
        parent[i] = findADryDayStartingFrom(parent[i]);
        return parent[i];
    }

    public void findNextDryDayAndAssignAsParentOf(int i) {
        // 'i'is the day we are evaluatin for
        int startingIndexToFindAFutureDryDay = i+1;
        parent[i] = findADryDayStartingFrom(startingIndexToFindAFutureDryDay);
    }
}
