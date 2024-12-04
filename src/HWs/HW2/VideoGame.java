package HWs.HW2;

/**
 * Class: VideoGame
 * @author Hugo
 * @version 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: October 6th, 2024
 *
 * This class – This is our VideoGame Class, which is used to model the details of a video game, including its name, platform, and rating.
 * The class includes validation for proper input, and ensures that the name is not empty, the platform is one of the valid values (Xbox, PS5, PC),
 * and the rating is either E, T, or M. The class implements `Comparable` to allow for sorting based on the name of the video game, and overrides
 * `hashCode()` and `equals()` to ensure no duplicate games are entered into a set. The class also provides a `toString()` method for displaying
 * game details in a user-friendly format.
 */

import java.io.Serializable;
import java.util.Objects;

public class VideoGame implements Comparable<VideoGame>, Serializable {
    private String name;
    private String platform;
    private String rating;

    public VideoGame(String name, String platform, String rating) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (!platform.equals("Xbox") && !platform.equals("PS5") && !platform.equals("PC")) {
            throw new IllegalArgumentException("Platform must be Xbox, PS5, or PC");
        }
        if (!rating.equals("E") && !rating.equals("T") && !rating.equals("M")) {
            throw new IllegalArgumentException("Rating must be E, T, or M");
        }
        this.name = name;
        this.platform = platform;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getPlatform() {
        return platform;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGame videoGame = (VideoGame) o;
        return name.equals(videoGame.name) && platform.equals(videoGame.platform) && rating.equals(videoGame.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, platform, rating);
    }

    @Override
    public int compareTo(VideoGame other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "VideoGame{name='" + name + "', platform='" + platform + "', rating='" + rating + "'}";
    }
}

