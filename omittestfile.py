import cv2
import numpy as np
from PIL import Image
import pytesseract
from picamera2 import Picamera2
# Initialize the camera
picam2 = Picamera2()
def preview_and_capture():
    """Preview the camera feed in an OpenCV window and capture an image when Enter is pressed."""
    picam2.start()
    print("Previewing... Press Enter to capture the image or 'q' to quit.")
    captured_image = None
    while True:
        # Capture a frame from the camera
        frame = picam2.capture_array()
        # Display the frame in an OpenCV window
        cv2.imshow("Camera Preview", frame)
        # Wait for a key press
        key = cv2.waitKey(1) & 0xFF
        if key == ord('\r'):  # Press Enter to capture
            print("Image captured!")
            captured_image = frame
            break
        elif key == ord('q'):  # Press 'q' to quit preview
            print("Exiting preview without capturing.")
            break
    cv2.destroyAllWindows()  # Close the OpenCV window
    picam2.stop()
    return captured_image

def preprocess_image(image):
    """Convert the image to grayscale and apply thresholding."""
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    _, thresh = cv2.threshold(gray, 150, 255, cv2.THRESH_BINARY_INV)
    return thresh

def crop_grid(image):
    """Manually crop the grid area from the image."""
    height, width = image.shape[:2]
    x_start, x_end = int(0.3 * width), int(0.7 * width)
    y_start, y_end = int(0.3 * height), int(0.7 * height)
    cropped = image[y_start:y_end, x_start:x_end]
    return cropped

def split_into_tiles(grid_image):
    """Split the grid image into 16 individual tiles."""
    tiles = []
    grid_size = 4  # Assuming a 4x4 grid
    tile_height = grid_image.shape[0] // grid_size
    tile_width = grid_image.shape[1] // grid_size
    for i in range(grid_size):
        for j in range(grid_size):
            x_start, x_end = j * tile_width, (j + 1) * tile_width
            y_start, y_end = i * tile_height, (i + 1) * tile_height
            tile = grid_image[y_start:y_end, x_start:x_end]
            tiles.append(tile)
    return tiles

def extract_letters(tiles):
    """Extract letters from each tile using OCR."""
    letters = []
    config = r'--psm 10 -c tessedit_char_whitelist=ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    for idx, tile in enumerate(tiles):
        resized_tile = cv2.resize(tile, (100, 100), interpolation=cv2.INTER_LINEAR)
        pil_image = Image.fromarray(resized_tile)
        pil_image.save(f"tile_{idx + 1}.png")
        text = pytesseract.image_to_string(pil_image, config=config).strip()
        if len(text) == 1 and text.isalpha():
            letters.append(text)
        else:
            letters.append('?')
        print(f"Tile {idx + 1}: {text}")
    return letters

def main():
    print("Starting the program...")
    # Preview and capture the image
    print("Launching camera preview...")
    image = preview_and_capture()
    if image is None:
        print("No image captured. Exiting program.")
        return
    # Preprocess the image
    print("Preprocessing the image...")
    processed_image = preprocess_image(image)
    # Crop the grid area
    print("Cropping the grid area...")
    cropped_grid = crop_grid(processed_image)
    # Split the grid into tiles
    print("Splitting the grid into tiles...")
    tiles = split_into_tiles(cropped_grid)
    # Extract letters from the tiles
    print("Extracting letters...")
    letters = extract_letters(tiles)
    # Print the final result
    print("Final Letter Grid (Row by Row):")
    print(" ".join(letters))
if _name_ == "_main_":
    main()