package ICs.IC6;

import java.text.DecimalFormat;

/**
 * Class Score
 * This class tracks the score and must be modified to be thread safe.
 */
public class Score
{
	private long id;
	private double score;
	private DecimalFormat df = new DecimalFormat("#,###.##");
	
	/**Grade
	 * @param id
	 * @param score
	 */
	public Score(long id, double score)
	{
		this.id = id;
		this.score = score;
	}

	/** Method getId
	 * @return
	 */
	public long getId()
	{
		return id;
	}

	/** Method setId
	 * @param id
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/** Method getScore
	 * @return
	 */
	public double getScore()
	{
		return score;
	}

	/** Method setScore
	 * @param score
	 */
	public void setScore(double score)
	{
		this.score = score;
	}
	
	public synchronized void updateScore(double change)
	{
		score += change;
		System.out.println("ID: " + id + "\t" + " Score: " + df.format(score));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return id + "\t" + "\t" + score;
	}
}
