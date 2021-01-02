import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private Map<String, Integer> teamIdByName;
    private String[] teamNames;
    private int[] w; // wins
    private int[] l; // losses
    private int[] r; // remaining games
    private int[][] g; // games left to play against team j


    /**
     * Create a baseball division from given filePath in format specified below:
     * The input format is the number of teams in the division n followed by one line for each team.
     * Each line contains the team name (with no internal whitespace characters),
     * the number of wins, the number of losses, the number of remaining games,
     * and the number of remaining games against each team in the division.
     * For example:
     * % cat teams4.txt
     * 4
     * Atlanta       83 71  8  0 1 6 1
     * Philadelphia  80 79  3  1 0 0 2
     * New_York      78 78  6  6 0 0 0
     * Montreal      77 82  3  1 2 0 0
     * @param filePath the path to the file containing team name, number of wins, number of losses,
     *                 number of remaining games, and the number of remaining games against each team in the division.
     */
    public BaseballElimination(String filePath) {
        In in = new In(filePath);
        int numberOfTeams = in.readInt();
        teamIdByName = new HashMap<>(numberOfTeams);
        teamNames = new String[numberOfTeams];
        w = new int[numberOfTeams];
        l = new int[numberOfTeams];
        r = new int[numberOfTeams];
        g = new int[numberOfTeams][numberOfTeams];

        for (int i = 0; i < numberOfTeams; i++) {
            String name = in.readString();
            teamNames[i] = name;
            teamIdByName.put(name, i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < numberOfTeams; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    /**
     * Return the number of teams
     * @return
     */
    public int numberOfTeams() {
        return teamNames.length;
    }

    /**
     * Return an iterator that iterates over the teams names
     * @return an iterator that iterates over the teams names.
     */
    public Iterable<String> teams() {
        List<String> result = new LinkedList<>();
        result.addAll(teamIdByName.keySet());
        return result;
    }

    /**
     * Return the number of wins for given team
     * @param team
     * @return the number of wins for the given team
     */
    public int wins(String team) {
        if (!teamIdByName.containsKey(team)) {
            throw new IllegalArgumentException("Not a valid team");
        }
        return w[teamIdByName.get(team)];
    }


    /**
     * Return the number of losses for given team
     * @param team
     * @return the number of losses for given team
     */
    public int losses(String team) {
        if (!teamIdByName.containsKey(team)) {
            throw new IllegalArgumentException("in losses()");
        }
        return l[teamIdByName.get(team)];
    }

    /**
     * Return the number of remaining games for given team
     * @param team
     * @return the number of remaining games for given team
     */
    public int remaining(String team) {
        if (!teamIdByName.containsKey(team)) {
            throw new IllegalArgumentException("in remaining()");
        }
        return r[teamIdByName.get(team)];
    }

    /**
     * Return the number of remaining games between {@code team1} and {@code team2}.
     * @param team1
     * @param team2
     * @return the number of remaining games between {@code team1} and {@code team2}.
     */
    public int against(String team1, String team2) {
        if (!teamIdByName.containsKey(team1)) {
            throw new IllegalArgumentException("Team1 is not a valid team");
        }
        if (!teamIdByName.containsKey(team2)) {
            throw new IllegalArgumentException("team2 is not a valid team");
        }
        return g[teamIdByName.get(team1)][teamIdByName.get(team2)];
    }

    //

    /**
     * Return is given team eliminated.
     * To check whether team x is eliminated, we consider two cases.
     * Trivial elimination:
     * If the maximum number of games team x can win
     * is less than the number of wins of some other team i, then team x is trivially eliminated
     *
     * Nontrivial elimination:
     * we create a flow network and solve a maxflow problem in it.
     * In the network, feasible integral flows correspond to outcomes of the remaining schedule.
     * There are vertices corresponding to teams (other than team x) and to remaining divisional games (not involving team x).
     * Intuitively, each unit of flow in the network corresponds to a remaining game.
     * As it flows through the network from s to t, it passes from a game vertex, say between teams i and j,
     * then through one of the team vertices i or j, classifying this game as being won by that team.
     * @param team
     * @return true if this team is eliminated; false otherwise
     */
    public boolean isEliminated(String team) {
        if (!teamIdByName.containsKey(team)) {
            throw new IllegalArgumentException("Team passed is not valid");
        }
        int teamID = teamIdByName.get(team);
        // Trivial elimination
        for (int i = 0; i < this.numberOfTeams(); i++) {
            if (w[teamID] + r[teamID] < w[i]) return true;
        }

        //Nontrivial elimination
        FlowNetwork fn = buildFlowNetwork(teamID);
        FordFulkerson f = new FordFulkerson(fn, 0, fn.V() - 1);
        for (FlowEdge edge : fn.adj(0)) {
            if (edge.flow() != edge.capacity()) {
                return true;
            }
        }
        return false;
    }

    /**
     * A helper method that creates a flow network with a given team id
     * @param teamID
     * @return a flow network built by the given team id.
     */
    private FlowNetwork buildFlowNetwork(int teamID) {
        int N = 2 + (this.numberOfTeams() * (this.numberOfTeams() - 1)) / 2;
        FlowNetwork fn = new FlowNetwork(N);
        int count = 1;
        for (int i = 0; i < this.numberOfTeams() - 1; i++) {
            if (i != teamID) {
                for (int j = i + 1; j < this.numberOfTeams(); j++) {
                    if (j != teamID) {
                        fn.addEdge(new FlowEdge(0, count, g[i][j]));
                        if (i > teamID) {
                            fn.addEdge(new FlowEdge(count, N - numberOfTeams() + i - 1, Double.POSITIVE_INFINITY));
                        } else {
                            fn.addEdge(new FlowEdge(count, N - numberOfTeams() + i, Double.POSITIVE_INFINITY));
                        }
                        if (j > teamID) {
                            fn.addEdge(new FlowEdge(count, N - numberOfTeams() + j - 1, Double.POSITIVE_INFINITY));
                        } else {
                            fn.addEdge(new FlowEdge(count, N - numberOfTeams() + j, Double.POSITIVE_INFINITY));
                        }
                        count++;
                    }
                }
            }

        }
        for (int i = 0; i < this.numberOfTeams(); i++) {
            if (i != teamID) {
                if (w[teamID] + r[teamID] - w[i] >= 0) {
                    fn.addEdge(new FlowEdge(count, N - 1, w[teamID] + r[teamID] - w[i]));
                } else {
                    fn.addEdge(new FlowEdge(count, N - 1, Double.POSITIVE_INFINITY));
                }
                count++;
            }
        }
        return fn;
    }

    /**
     * Return a subset R of teams that eliminates given team; null if not eliminated
     * @param team
     * @return a subset R of teams that eliminates given team; null if not eliminated
     */
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamIdByName.containsKey(team)) {
            throw new IllegalArgumentException("Invalid team input");
        }
        List<String> result = new LinkedList<>();
        boolean isEliminated = false;
        int teamID = teamIdByName.get(team);
        for (int i = 0; i < numberOfTeams(); i++) {
            if (w[teamID] + r[teamID] < w[i]) {
                isEliminated = true;
                result.add(teamNames[i]);
            }
        }
        FlowNetwork fn = buildFlowNetwork(teamID);
        FordFulkerson f = new FordFulkerson(fn, 0, fn.V() - 1);
        for (FlowEdge edge : fn.adj(0)) {
            if (edge.flow() != edge.capacity()) {
                isEliminated = true;
            }
        }
        if (!isEliminated) {
            return null;
        }
        int N = 2 + (this.numberOfTeams() * (this.numberOfTeams() - 1)) / 2 - this.numberOfTeams();
        for (int i = 0; i < this.numberOfTeams(); i++) {
            if (i != teamID && f.inCut(N + i - (i > teamID ? 1 : 0))) result.add(teamNames[i]);
        }
        return result;
    }
}