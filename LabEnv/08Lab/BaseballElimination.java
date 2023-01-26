/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.TreeMap;

public class BaseballElimination {
    private final int n;
    private final TreeMap<Integer, String> idx2Team;
    private final TreeMap<String, Integer> team2Idx;
    private final int[] wins;

    private final int[] loses;
    private final int[] remains;
    private final int[] games; // i vs j => i * n + j;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        idx2Team = new TreeMap<>();
        team2Idx = new TreeMap<>();
        wins = new int[n];
        loses = new int[n];
        remains = new int[n];
        games = new int[n * n];
        for (int i = 0; i < n; i++) {
            String team = in.readString();
            idx2Team.put(i, team);
            team2Idx.put(team, i);
            wins[i] = in.readInt();
            loses[i] = in.readInt();
            remains[i] = in.readInt();
            for (int j = 0; j < n; j++)
                games[i * n + j] = in.readInt();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return team2Idx.keySet();
    }

    private void validate(String team) {
        if (team == null || !team2Idx.containsKey(team)) throw new IllegalArgumentException();
    }

    // number of wins for given team
    public int wins(String team) {
        validate(team);
        return wins[team2Idx.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        validate(team);
        return loses[team2Idx.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validate(team);
        return remains[team2Idx.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validate(team2);
        validate(team1);
        return games[team2Idx.get(team1) * n + team2Idx.get(team2)];
    }

    private class FFFN {
        private ArrayList<String> certificate;
        private boolean isEliminated;

        FFFN(String team) {
            certificate = new ArrayList<>();
            isEliminated = false;

            if (n == 1) return; // only one team
            if (isTrivialEliminated(team)) return;

            int queryIndex = team2Idx.get(team);

            int nV = nCr2(n - 1) + n - 1 + 2; // number of vertices
            int fullFlow = 0; // fill flow number
            FlowNetwork flowNetwork = new FlowNetwork(nV);
            TreeMap<Integer, Integer> teamID2GraphID = new TreeMap<>();
            for (int i = 0, j = 0; i < n; i++) {
                if (i == queryIndex) continue;
                int gID = j++ + nCr2(n - 1) + 1;
                teamID2GraphID.put(i, gID);
                FlowEdge edge = new FlowEdge(gID, nV - 1,
                                             wins[queryIndex] + remains[queryIndex] - wins[i]);
                flowNetwork.addEdge(edge);
            }
            for (int i = 0, idx = 1; i < n; i++) {
                if (i == queryIndex) continue;
                for (int j = i + 1; j < n; j++) {
                    if (j == queryIndex) continue;
                    FlowEdge edge = new FlowEdge(0, idx, games[i * n + j]);
                    fullFlow += games[i * n + j];
                    FlowEdge edge1 = new FlowEdge(idx, teamID2GraphID.get(i),
                                                  Double.POSITIVE_INFINITY);
                    FlowEdge edge2 = new FlowEdge(idx, teamID2GraphID.get(j),
                                                  Double.POSITIVE_INFINITY);
                    idx++;
                    flowNetwork.addEdge(edge);
                    flowNetwork.addEdge(edge1);
                    flowNetwork.addEdge(edge2);
                }
            }
            // calculate max flow
            FordFulkerson ff = new FordFulkerson(flowNetwork, 0, nV - 1);
            // calculate certificate of elimination
            for (int i = 0, j = 0; i < n; i++) {
                if (i == queryIndex) continue;
                int gID = j++ + nCr2(n - 1) + 1;
                if (ff.inCut(gID))
                    certificate.add(idx2Team.get(i));
            }
            isEliminated = ff.value() < fullFlow;
        }

        private int nCr2(int k) {
            return k * (k - 1) / 2;
        }

        private boolean isTrivialEliminated(String team) {
            int idx = team2Idx.get(team);
            int totalWins = wins[idx] + remains[idx];
            for (int i = 0; i < n; i++) {
                if (totalWins < wins[i]) {
                    certificate.add(idx2Team.get(i));
                    isEliminated = true;
                    return true;
                }
            }
            return false;
        }
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validate(team);
        FFFN fffn = new FFFN(team);
        return fffn.isEliminated;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        FFFN fffn = new FFFN(team);
        if (fffn.certificate.isEmpty()) return null;
        return fffn.certificate;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
