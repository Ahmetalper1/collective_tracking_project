package game;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.logging.Level;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import logger.LoggerInstance;

/*
 * This class represents the mathematical computation that allows us to get
 * the equation of a block movement via its point history. We are using here
 * Lagrange's interpolation.
 */
public class HistoryAnalyzer
{
	// This represents the minimum center point nb to begin the interpolation.
	public static int MINIMUM_POINT_HISTORY = 2;
	
	/*
	 * That function is the one that is called to compute and find the right
	 * equation. The string returned is the equation in a text format.
	 */
	public static String analyzeBlock(Block block)
	{	
		// Error case if the block is null (should never happen).
		if (block == null || block.getXHistory().isEmpty())
		{
			LoggerInstance.LOGGER.log(Level.SEVERE,
					"Tried to analyze a null or empty block ! "
					+ "(See Issue on Gitlab for empty block problem.)");
			
			return "A null or empty block can not be analyzed.";
		}
		
		LoggerInstance.LOGGER.log(Level.FINEST, 
				"Analyzing block " + block.getID());
		
		// Computes a single point if there are multiple on a same X coordinate.
		//flattenPoints(block);//ada commented -- no need for flatten points, or?
			
		// Error case if there is not enough points in the history of the block.
		if (verifyBlock(block) == 1)
		{
			LoggerInstance.LOGGER.log(Level.SEVERE,
					"Block " + block.getID() +
					" does not meet the requirements for the interpolation !");
			
			return "This block is static at point ("
				+ block.getXHistory().get(0) + ';' + block.getYHistory().get(0)
				+ "), no function possible.";
		}
		
		/*
		 * This error case is detecting if the block has two or more different
		 * points for the same X coordinate, which makes it impossible to
		 * analyze and get a function via interpolation.
		 */
		/* ada: when using linear regression, we do not need this check (only when using interpolation).
		if (verifyBlock(block) == 2)
		{
			LoggerInstance.LOGGER.log(Level.SEVERE, "Block " + block.getID()
				+ "'s points are following a forbidden behavior !");
			
			return "There was a forbidden behavior in the block movements:"
					+ " it has two or more points on the same X coordinate.";
		}
		*/
		
		
		LoggerInstance.LOGGER.log(Level.FINEST, "Launching interpolation on" 
				+ "block " + block.getID() + "...");
		
		// Getting the result function from the alias method.
		//PolynomialFunction function = lagrangeInterpolation(block);
		
		/*
		 * Getting the result as a linear function, thanks to the
		 * linearLeastSquaresRegression() method.
		 */
		PolynomialFunction function = linearLeastSquaresRegression(block);
		//ada added: add the function to the block
		block.setBlockTrajectoryFunction(function);
		
		LoggerInstance.LOGGER.log(Level.FINEST, "Finished the interpolation" +
				" for block " + block.getID() + "...");
		
		DecimalFormat DF = new DecimalFormat("#.##");
		DF.setRoundingMode(RoundingMode.CEILING);
		
		// Building the result string to return.
		String result = "The block was successfully analyzed. The equation of"
			+ " the movement is : ";
		
		if (function.getCoefficients().length == 2)
		{
			result += DF.format(Double.valueOf(function.getCoefficients()[1]))
					+ "x ";
		}
		
		if (function.getCoefficients()[0] > 0)
			result += "+ ";
		
		result += DF.format(Double.valueOf(function.getCoefficients()[0]));
		
		return result;
	}
	
	/*
	 * The goal of this function is to verify that it is possible to apply an
	 * interpolation on the block movement history and that there is enough
	 * points to do so.
	 */
	private static int verifyBlock(Block block)
	{
		/*
		 *  If there is only one point (or even 0 ?), it's impossible to
		 *  determine the equation.
		 */
		if (block.getXHistory().size() < MINIMUM_POINT_HISTORY)
			return 1;
		
		/*
		 * For each center point recorded in the history of the block, we verify
		 * that there is no previous center point declared with the same X
		 * coordinate.
		 */
		for (int i = 0; i < block.getXHistory().size(); i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (Math.abs(block.getXHistory().get(i)
						- block.getXHistory().get(j)) < Double.valueOf("0.01"))
				{		
					return 2;
				}
			}
		}
		
		// Everything is good, we can start the interpolation.
		return 0;
	}
	
	/*
	 * This flattens the points in the case there are multiple points on one
	 * X coordinate.
	 */
	private static void flattenPoints(Block block)
	{
		LoggerInstance.LOGGER.log(Level.FINEST, "Flattening points of block " + 
				block.getID());
		
		// For every point in the history.
		for (int i = 0; i < block.getXHistory().size() - 1; i++)
		{
			/*
			 * This counter represents the number of points we encountered in
			 * the full history that has the same X coordinate as the one
			 * at index i. We start it at 1 because we take the current one in.
			 */
			int counter = 1;
			
			// For every following point, check if the same X coordinate.
			for (int j = i + 1; j < block.getXHistory().size(); j++)
			{
				if (Math.abs(block.getXHistory().get(i)
						- block.getXHistory().get(j)) < Double.valueOf("0.01"))
				{
					/*
					 * Adding the Y coordinate to the current point, and
					 * deleting it.
					 */
					block.getYHistory().set(i, (block.getYHistory().get(i)
							+ block.getYHistory().get(j)) / 2);
					block.getXHistory().remove(j);
					block.getYHistory().remove(j);
					
					// Updating counter.
					counter++;
					j--;
				}
			}
			
			// Dividing the sum of Y coordinates by the number of points.
			block.getYHistory().set(i, block.getYHistory().get(i) / counter);
		}
	}
	
	/*
	 * This function applies the Lagrange Interpolation in order to find
	 * the right equation of a polynomial function that goes through all the
	 * points. Not used atm...
	 */
	@SuppressWarnings("unused")
	private static PolynomialFunction lagrangeInterpolation(Block block)
	{
		// Creating the final polynomial we are going to return.
		double[] defaultPolyCoef = new double[block.getXHistory().size()];
		for (int i = 0; i < block.getXHistory().size(); i++)
			defaultPolyCoef[i] = 0;
		PolynomialFunction function = new PolynomialFunction(defaultPolyCoef);
		
		/*
		 *  Index used to know which lagrange polynomial function we are
		 *  currently working on. Please see the Lagrange Interpolation
		 *  explanation for details.
		 */
		for (int lagrangePolyIndex = 0;
				lagrangePolyIndex < block.getXHistory().size();
				lagrangePolyIndex++)
		{
			// Creating the default lagrange polynomial function to 1.
			double[] coefficients = new double[1];
			coefficients[0] = 1;
			PolynomialFunction lagrangePoly =
					new PolynomialFunction(coefficients);
			
			/*
			 *  This represents the denominator of the whole lagrange polynomial
			 *  function we are currently working with.
			 */
			Double divTotal = 1.;
			
			/*
			 * We here compute the whole lagrange polynomial function of index
			 * lagrangePolyIndex.
			 */
			for (int i = 0; i < block.getXHistory().size(); i++)
			{
				// If the i index is correct. (cf. Lagrange Interpolation)
				if (i != lagrangePolyIndex)
				{
					// Creating an element of the sum.
					double[] elt = new double[2];
					
					// The negation of the i-th X coordinate.
					elt[0] = -block.getXHistory().get(i);
					
					// 1 at index 1 to represent X.
					elt[1] = 1;
					PolynomialFunction tempPoly =
							new PolynomialFunction(elt);
					
					/*
					 *  Multiplying the lagrangePolyIndex-th poly by the one
					 *  we just created.
					 */
					lagrangePoly = lagrangePoly.multiply(tempPoly);
					
					/*
					 *  Updating the denominator of the current poly.
					 *  (Again cf. Lagrange interpolation.)
					 */
					divTotal *= (block.getXHistory().get(lagrangePolyIndex)
							- block.getXHistory().get(i));
				}
			}
			
			// Creating the poly that represents the WHOLE denominator.
			double[] divArray = new double[1];
			divArray[0] = block.getYHistory().get(lagrangePolyIndex) / divTotal;
			PolynomialFunction div = new PolynomialFunction(divArray);
			
			// Adding the current poly to the final polynomial function.
			function = function.add(lagrangePoly.multiply(div));
		}
		
		return function;
	}
	
	/*
	 * This function is able to find the linear function that is the closest
	 * in average to every history point of the block. It sadly does not
	 * work well for circular movements for example.
	 */
	private static PolynomialFunction linearLeastSquaresRegression(Block block)
	{
		// Creating the points that will be use to calculate.
		double[][] points = new double[block.getYHistory().size()][2];
		
		// Filling the array with the block's history points.
		for (int i = 0; i < block.getYHistory().size(); i++)
		{
			points[i][0] = block.getXHistory().get(i);
			points[i][1] = block.getYHistory().get(i);
		}
		
		/*
		 * Creating the Simple Regression and filling it with the previously
		 * created points array.
		 */
		SimpleRegression SR = new SimpleRegression(true);
		SR.addData(points);
		
		// Retrieving the results from the Simple Regression.
		double[] poly = new double[2];
		poly[0] = SR.getIntercept();
		poly[1] = SR.getSlope();
		
		// Creating the result function.
		PolynomialFunction function = new PolynomialFunction(poly);
		
		return function;
	}
}
