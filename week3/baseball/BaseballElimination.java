import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private final int numTeams;
    private final String[] teams;
    private final Map<String, Integer> teamNumbers;
    private final int[] wins;
    private final int[] loses;
    private final int[] remaining;
    // unique pair of team1 vs team2 pair to number of games left to play
    private final Map<Set<Integer>, Integer> uniqueGames;
    private final List<List<Integer>> certificates;
    private final Map<Integer, Boolean> eliminatedCache;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        numTeams = in.readInt();
        teams = new String[numTeams];
        teamNumbers = new HashMap<>();
        wins = new int[numTeams];
        loses = new int[numTeams];
        remaining = new int[numTeams];
        uniqueGames = new HashMap<>();
        certificates = new ArrayList<>(numTeams);
        for (int i = 0; i < numTeams; i++) {
            certificates.add(null);
        }
        eliminatedCache = new HashMap<>();

        for (int i = 0; i < numTeams; i++) {
            String team = in.readString();
            teams[i] = team;
            teamNumbers.put(team, i);

            wins[i] = in.readInt();
            loses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < numTeams; j++) {
                int gamesLeft = in.readInt();
                if (i != j) {
                    uniqueGames.put(new HashSet<>(Arrays.asList(i, j)), gamesLeft);
                }
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.length;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeam(team);

        int i = teamNumbers.get(team);
        return wins[i];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeam(team);

        int i = teamNumbers.get(team);
        return loses[i];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeam(team);

        int i = teamNumbers.get(team);
        return remaining[i];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);

        if (team1.equals(team2)) {
            return 0;
        }

        int i = teamNumbers.get(team1);
        int j = teamNumbers.get(team2);

        return uniqueGames.get(new HashSet<>(Arrays.asList(i, j)));
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeam(team);

        int x = teamNumbers.get(team);

        if (eliminatedCache.containsKey(x)) {
            return eliminatedCache.get(x);
        }

        // 1 for source + 1 for sink + number of teams - 1 for given team
        int numVertices = 2 + numTeams - 1;

        // number of games without given team
        int numGames = 0;
        for (Map.Entry<Set<Integer>, Integer> game : uniqueGames.entrySet()) {
            // skip games with x team
            if (game.getKey().contains(x)) {
                continue;
            }

            numGames++;
            // add vertex for each game without given team
            numVertices++;
        }

        FlowNetwork flowNetwork = new FlowNetwork(numVertices);

        List<Integer> certificate = new ArrayList<>();

        int[] teamVertices = new int[numTeams];
        // team vertices start right after game vertices
        int currTeamVertex = numGames + 1;
        for (int i = 0; i < numTeams; i++) {
            // skip given team
            if (i == x) {
                continue;
            }

            if (wins[x] + remaining[x] < wins[i]) {
                certificate.add(i);
                continue;
            }

            teamVertices[i] = currTeamVertex;

            // add edge from team to sink
            flowNetwork.addEdge(new FlowEdge(currTeamVertex, numVertices - 1, wins[x] + remaining[x] - wins[i]));

            currTeamVertex++;
        }

        if (!certificate.isEmpty()) {
            certificates.set(x, certificate);
            eliminatedCache.put(x, Boolean.TRUE);
            return true;
        }

        int totalGamesLeft = 0;

        int gameVertex = 1;
        for (Map.Entry<Set<Integer>, Integer> game : uniqueGames.entrySet()) {
            // skip games with x team
            if (game.getKey().contains(x)) {
                continue;
            }

            totalGamesLeft += game.getValue();

            // add edge from source to game vertex with games left as a capacity
            flowNetwork.addEdge(new FlowEdge(0, gameVertex, game.getValue()));

            // add edges from game to teams participating in it
            for (int i : game.getKey()) {
                flowNetwork.addEdge(new FlowEdge(gameVertex, teamVertices[i], Double.POSITIVE_INFINITY));
            }

            gameVertex++;
        }

        FordFulkerson maxFlow = new FordFulkerson(flowNetwork, 0, numVertices - 1);

        if (totalGamesLeft == maxFlow.value()) {
            eliminatedCache.put(x, Boolean.FALSE);
            return false;
        }

        for (int i = 0; i < numTeams; i++) {
            // skip given team
            if (i == x) {
                continue;
            }

            if (maxFlow.inCut(teamVertices[i])) {
                certificate.add(i);
            }
        }

        certificates.set(x, certificate);
        eliminatedCache.put(x, Boolean.TRUE);
        return true;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!isEliminated(team)) {
            return null;

        }

        List<Integer> certificate = certificates.get(teamNumbers.get(team));
        List<String> certificateTeams = new ArrayList<>(certificate.size());
        for (int i : certificate) {
            certificateTeams.add(teams[i]);
        }

        return certificateTeams;
    }

    private void checkTeam(String team) {
        if (!teamNumbers.containsKey(team)) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        // System.out.println(division.isEliminated("Philadelphia"));
        // System.out.println(division.certificateOfElimination("Philadelphia"));
        System.out.println(division.against("Atlanta", "Atlanta"));
    }
}
