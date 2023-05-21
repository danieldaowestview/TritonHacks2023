import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

public class Star {
	
	private String fileName;
	private String path;
	private int height;
	private int width;
	private int[][][] pixels;
	private Color avgColor;
	private double temperature = -1.0;
	private double luminosity = -1.0;
	private double radius = -1.0;
	private double absoluteMagnitude = -1.0;
	private String starColor;
	private String spectralClass;
	
	public Star(String path) {
		
		fileName = path.substring(path.lastIndexOf("\\") + 1);
		Dimension dim = getImageDim();
		width = dim.width;
		height = dim.height;
		pixels = getPixels(toBufferedImage(getImage(fileName)));
		getAvgColor();
		temperature = estimateTemperature();
		setStarColor();
		radius = approximateRadius();
		luminosity = calculateLuminosity();
		spectralClass = approximateSpectralClass();
		
	}
	
	public String approximateSpectralClass() {
		int avgRed = avgColor.getRed();
		int avgGreen = avgColor.getGreen();
		int avgBlue = avgColor.getBlue();
		
		
		
        // Calculate the color index (B-V) using average RGB values
        double colorIndex = calculateColorIndex(avgRed, avgGreen, avgBlue);

        // Calculate the absolute magnitude using luminosity and radius
        absoluteMagnitude = calculateAbsoluteMagnitude(luminosity, radius);

        // Approximate the spectral class based on the color index and absolute magnitude
        String spectralClass;

        if (temperature >= 30000 && colorIndex < -0.4) {
            spectralClass = "O";
        } else if (temperature >= 10000 && colorIndex < 0.0) {
            spectralClass = "B";
        } else if (temperature >= 7500 && colorIndex < 0.4) {
            spectralClass = "A";
        } else if (temperature >= 6000 && colorIndex < 0.8) {
            spectralClass = "F";
        } else if (temperature >= 5200 && colorIndex < 1.2) {
            spectralClass = "G";
        } else if (temperature >= 3700 && colorIndex < 1.6) {
            spectralClass = "K";
        } else if (temperature < 3700 || colorIndex >= 1.6) {
            spectralClass = "M";
        } else {
            spectralClass = "Unknown";
        }

        return spectralClass;
    }

    public double calculateColorIndex(int avgRed, int avgGreen, int avgBlue) {
        double r = avgRed / 255.0;
        double g = avgGreen / 255.0;
        double b = avgBlue / 255.0;

        // Calculate the color index (B-V) using average RGB values
        double colorIndex = 0.92 * (r - g) + 0.98 * (g - b);

        return colorIndex;
    }

    public double calculateAbsoluteMagnitude(double luminosity, double radius) {
        if (absoluteMagnitude != -1.0) return absoluteMagnitude;
    	
    	// Calculate the absolute magnitude using luminosity and radius
        double absoluteMagnitude = 4.83 - 2.5 * Math.log10(luminosity) - 2.5 * Math.log10(radius);

        return absoluteMagnitude;
    }
	
	public double calculateLuminosity() {
        if (luminosity != -1.0) return luminosity;
		
		double luminosity;

        // Assuming temperature is in Kelvin and radius is relative to the Sun's radius
        switch (starColor.toLowerCase()) {
            case "red":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "blue white":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "white":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "yellowish white":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "pale yellow orange":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "blue":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "whitish":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "yellow-white":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "orange":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "white-yellow":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "blue-white":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            case "yellowish":
                luminosity = Math.pow(radius, 2) * Math.pow(temperature / 5778.0, 4);
                break;
            default:
                luminosity = -1.0;  // Unknown starColor, return a negative value or throw an exception
                break;
        }

        return luminosity;
    }
	
	public double approximateRadius() {
        if (radius != -1.0) return radius;
		
		double radius;
		
		int redValue = avgColor.getRed();
		int blueValue = avgColor.getBlue();
		int greenValue = avgColor.getGreen();
		
        // Assuming temperature is in Kelvin
        if (temperature >= 0) {
            radius = 1.0 * Math.sqrt(temperature); // Default radius factor
            if (redValue > greenValue && greenValue > blueValue) {
                radius = 0.7 * Math.sqrt(temperature);
            } else if (blueValue > redValue && blueValue > greenValue) {
                radius = 2.0 * Math.sqrt(temperature);
            } else if (Math.abs(redValue - greenValue) <= 10 && Math.abs(redValue - blueValue) <= 10) {
                radius = 1.0 * Math.sqrt(temperature);
            } else if (redValue > greenValue && greenValue > blueValue) {
                radius = 1.2 * Math.sqrt(temperature);
            } else if (redValue > greenValue && greenValue > blueValue && redValue - blueValue > 50) {
                radius = 1.4 * Math.sqrt(temperature);
            } else if (Math.abs(redValue - greenValue) <= 20 && Math.abs(redValue - blueValue) <= 20 && Math.abs(greenValue - blueValue) <= 20) {
                radius = 1.0 * Math.sqrt(temperature);
            } else if (redValue > greenValue && redValue > blueValue && greenValue - blueValue > 20) {
                radius = 1.1 * Math.sqrt(temperature);
            } else if (redValue > greenValue && redValue > blueValue) {
                radius = 1.3 * Math.sqrt(temperature);
            } else if (Math.abs(redValue - greenValue) <= 10 && Math.abs(redValue - blueValue) <= 10 && redValue > blueValue) {
                radius = 1.2 * Math.sqrt(temperature);
            } else if (blueValue > redValue && blueValue > greenValue) {
                radius = 1.5 * Math.sqrt(temperature);
            } else if (greenValue > redValue && greenValue > blueValue && greenValue - redValue > 20) {
                radius = 1.2 * Math.sqrt(temperature);
            }
        } else {
            radius = -1.0; // Invalid temperature, return a negative value or throw an exception
        }

        return radius;
    }
	
	public double estimateTemperature() {
        if (temperature != -1.0) return temperature;
		
		double r = avgColor.getRed() / 255.0;
        double g = avgColor.getGreen() / 255.0;
        double b = avgColor.getBlue() / 255.0;

        // Calculate the color temperature using the color-temperature relationship
        double temp = 0;

        if (r > g && g > b) {
            temp = 6600 * Math.pow((r - 0.4) / (0.82 - 0.4), -0.43);
        } else if (g > r && r > b) {
            temp = 6600 * Math.pow((g - 0.4) / (0.82 - 0.4), -0.43);
        } else if (g > b && b > r) {
            temp = 4800 * Math.pow((g - 0.4) / (0.92 - 0.4), -0.35);
        } else if (b > g && g > r) {
            temp = 3000 * Math.pow((b - 0.25) / (0.8 - 0.25), -0.5);
        } else {
            // Default value if the RGB values do not fit the known color-temperature relationships
            temp = 5000;
        }

        return Math.round(temp);
    }
	
	private void setStarColor() {
		
		int redValue = avgColor.getRed();
		int blueValue = avgColor.getBlue();
		int greenValue = avgColor.getGreen();
		
		if (redValue - 50 > greenValue && redValue - 50 > blueValue) {
		    starColor = "Red";
		} else if (blueValue > redValue && blueValue > greenValue) {
		    starColor = "Blue-White";
		} else if (Math.abs(redValue - greenValue) <= 10 && Math.abs(redValue - blueValue) <= 10) {
		    starColor = "White";
		} else if (redValue > greenValue && greenValue > blueValue) {
		    starColor = "Yellowish-White";
		} else if (redValue > greenValue && greenValue > blueValue && redValue - blueValue > 50) {
		    starColor = "Pale Yellow-Orange";
		} else if (Math.abs(redValue - greenValue) <= 20 && Math.abs(redValue - blueValue) <= 20 && Math.abs(greenValue - blueValue) <= 20) {
		    starColor = "Whitish";
		} else if (redValue > greenValue && redValue > blueValue && greenValue - blueValue > 20) {
		    starColor = "Yellow-White";
		} else if (redValue > greenValue && redValue > blueValue) {
		    starColor = "Orange";
		} else if (Math.abs(redValue - greenValue) <= 10 && Math.abs(redValue - blueValue) <= 10 && redValue > blueValue) {
		    starColor = "White-Yellow";
		} else if (blueValue > redValue && blueValue > greenValue) {
		    starColor = "Blue-White";
		} else if (greenValue > redValue && greenValue > blueValue && greenValue - redValue > 20) {
		    starColor = "Yellowish";
		} else {
		    starColor = "Unknown";
		}
		
	}
	
	private Color getAvgColor() {
		if (avgColor != null) return avgColor;
		
		int avgR = 0;
		int avgB = 0;
		int avgG = 0;
		int avgA = 0;
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				avgR += pixels[i][j][0];
				avgB += pixels[i][j][1];
				avgG += pixels[i][j][2];
				avgA += pixels[i][j][3];
			}
		}
		
		int area = width * height;
		avgR = avgR / area;
		avgB = avgB / area;
		avgG = avgG / area;
		avgA = avgA / area;
		
		avgColor = new Color(avgR, avgB, avgG, avgA);
		return avgColor;
	}
	
	private int[][][] getPixels(BufferedImage image) {
	    int[][][] result = new int[height][width][4];
	    for (int x = 0; x < image.getWidth(); x++) {
	        for (int y = 0; y < image.getHeight(); y++) {
	            Color c = new Color(image.getRGB(x, y), true);
	            result[y][x][0] = c.getRed();
	            result[y][x][1] = c.getGreen();
	            result[y][x][2] = c.getBlue();
	            result[y][x][3] = c.getAlpha();
	        }
	    }
	    
	    return result;
	}
	
	private Dimension getImageDim() {
	    Dimension result = null;
	    String suffix = getFileSuffix(fileName);
	    Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
	    if (iter.hasNext()) {
	        ImageReader reader = iter.next();
	        try {
	            File file = new File(path);
	            if (!file.exists()) {
	                System.err.println("File does not exist: " + path);
	                return null;
	            }
	            ImageInputStream stream = new FileImageInputStream(file);
	            reader.setInput(stream);
	            int width = reader.getWidth(reader.getMinIndex());
	            int height = reader.getHeight(reader.getMinIndex());
	            result = new Dimension(width, height);
	        } catch (IOException e) {
	            System.err.println("Exception was caught, result remains null");
	            System.out.println(e.getMessage());
	            e.printStackTrace();
	        } finally {
	            reader.dispose();
	        }
	    } else {
	        System.out.println("No reader found for given format: " + suffix);
	    }
	    return result;
	}
	
	private String getFileSuffix(final String path) {
	    String result = null;
	    if (path != null) {
	        result = "";
	        if (path.lastIndexOf('.') != -1) {
	            result = path.substring(path.lastIndexOf('.'));
	            if (result.startsWith(".")) {
	                result = result.substring(1);
	            }
	        }
	    }
	    return result;
	}
	
	private Image getImage(String fileName) {
		Image tempImage = null;
		this.fileName = fileName;
		try {
			this.path = FileSystems.getDefault().getPath(new String("./")).toAbsolutePath().getParent() + "\\src\\" + fileName;
			URL imageURL = Star.class.getResource(fileName);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
	
	private static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

	public double getTemperature() {
		return temperature;
	}

	public double getLuminosity() {
		return luminosity;
	}

	public double getRadius() {
		return radius;
	}

	public double getAbsoluteMagnitude() {
		return absoluteMagnitude;
	}

	public String getStarColor() {
		return starColor;
	}

	public String getSpectralClass() {
		return spectralClass;
	}

	
	
}
