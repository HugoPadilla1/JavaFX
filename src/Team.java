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

    public void setTeamWins(int teamWins) {
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
}
