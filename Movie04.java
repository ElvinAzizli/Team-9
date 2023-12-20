public class Movie04 {
    private String title;
    private String director;
    private int releaseYear;
    private int runningTime;

    public Movie04(String title, String director, int releaseYear, int runningTime) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.runningTime = runningTime;
    }

    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public int getReleaseYear() { return releaseYear; }
    public int getRunningTime() { return runningTime; }

    public void setTitle(String title) { this.title = title; }
    public void setDirector(String director) { this.director = director; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public void setRunningTime(int runningTime) { this.runningTime = runningTime; }
}
