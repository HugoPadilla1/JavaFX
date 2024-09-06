/**Class: Team
 * @author Hugo Padilla
 * @version 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: September 5th, 2024
 *
 * This class – This class is our Team object which has teamName, teamWins, teamLosses, mascot, and returningPlayers.
 * The constructor utilizes the setters which each throw exceptions based on the validations required.
 * Each attribute for Team has exceptions to look for, as well as the Team object which checks for a total of 12 wins and losses combined.
 * The toString puts everything into words and displays after being initialized.
 */
public class Team {
    public String teamName;
    public int teamWins;
    public int teamLosses;
    public String mascot;
    public int returningPlayers;

    public Team(String teamName, int teamWins, int teamLosses, String mascot, int returningPlayers) throws TeamValidationException {
        setTeamName(teamName);
        setTeamWins(teamWins);
        setTeamLosses(teamLosses);
        setMascot(mascot);
        setReturningPlayers(returningPlayers);

        if ((teamWins + teamLosses) != 12) {
            throw new TeamValidationException("Wins and losses must add up to 12.");
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) throws TeamValidationException {
        if (teamName == null || teamName.trim().length() < 2) {
            throw new TeamValidationException("Team name must contain at least 2 characters.");
        }
        this.teamName = teamName.trim();
    }

    public int getTeamWins() {
        return teamWins;
    }

    public void setTeamWins(int teamWins) throws TeamValidationException{
        if (teamWins < 0 || teamWins > 12){
            throw new TeamValidationException("Wins must be between 0-12.");
        }
        this.teamWins = teamWins;
    }

    public int getTeamLosses() {
        return teamLosses;
    }

    public void setTeamLosses(int teamLosses) throws TeamValidationException{
        if (teamLosses < 0 || teamLosses > 12){
            throw new TeamValidationException("Losses must be between 0-12.");
        }
        this.teamLosses = teamLosses;
    }

    public String getMascot() {
        return mascot;
    }

    public void setMascot(String mascot) throws TeamValidationException{
        if (mascot == null || mascot.trim().isEmpty()) {
            throw new TeamValidationException("Mascot cannot be blank.");
        }
        this.mascot = mascot.trim();
    }

    public int getReturningPlayers() {
        return returningPlayers;
    }

    public void setReturningPlayers(int returningPlayers) throws TeamValidationException{
        if (returningPlayers < 0 || returningPlayers > 32) {
            throw new TeamValidationException("Number of returning players must be between 0 and 32.");
        }
        this.returningPlayers = returningPlayers;
    }

    @Override
    public String toString() {
        return "The " + teamName + " have a record of " + teamWins + "-" + teamLosses + ", with \n" + returningPlayers +
                " returning players and their mascot being the " + mascot + ".";
    }
}
