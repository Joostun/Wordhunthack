import cv2
import pytesseract
import numpy as np
from statistics import median
import imutils

pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files (x86)\Tesseract-OCR\tesseract.exe'


# img load
image_path = 'testIMGs\cropIMG_0393.PNG'
image = cv2.imread(image_path)

# Convert to YCrCb color space
ycrcb = cv2.cvtColor(image, cv2.COLOR_BGR2YCrCb)

# Define the lower and upper bounds for the color mask
lower_scalar = np.array([0.0, 125.0, 67.0])
upper_scalar = np.array([255.0, 173.0, 108.0])

# mask
# mask = cv2.inRange(ycrcb, lower_scalar, upper_scalar)
thresh = cv2.inRange(ycrcb, lower_scalar, upper_scalar)

# mask display
display_scale = 0.1
output_height, output_width = thresh.shape[:2]
new_width = int(output_width * display_scale)
new_height = int(output_height * display_scale)
small_mask = cv2.resize(thresh, (new_width, new_height), interpolation=cv2.INTER_AREA)

cv2.imshow('Color Mask', small_mask)
cv2.waitKey(0)
cv2.destroyAllWindows()

# --------------------------------------------------------------------------

img = image.copy()
# contours, _ = cv2.findContours(...) # Your call to find the contours using OpenCV 2.4.x
contours, hierarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE) # Your call to find the contours

filt_cont = []

for contour in contours:
    x, y, w, h = cv2.boundingRect(contour)
    # Calculate the aspect ratio
    area = cv2.contourArea(contour)
    perim = cv2.arcLength(contour, True)


    if perim == 0:  # Avoid division by zero
            continue
    
    circ = (4 * np.pi * area) / (perim ** 2)
    aspect_ratio = w / float(h)
    # Filter for square-like contours based on aspect ratio and size
    if 0.95<= aspect_ratio <= 1.05 and w > 220 and circ < .8:
        filt_cont.append(contour)

cv2.drawContours(img, filt_cont, -1, (255,0,0), 7)

small_cont = cv2.resize(img, (new_width, new_height), interpolation=cv2.INTER_AREA)

cv2.imshow("contour_img", small_cont)
cv2.waitKey(0)

filt_cont.reverse()
print(len(filt_cont))

final_string = ""
box_index = 1
for minibox in filt_cont:
    x,y,w,h = cv2.boundingRect(minibox)
    print(x, y, w, h)
    avg_side = (w + h)/2
    modif = int(avg_side * .1)
    crop_img = thresh[(y+modif):(y+h-modif), (x+modif):(x+w-modif)]
    cv2.imshow("crop_img", crop_img)
    letter_detect = pytesseract.image_to_string(crop_img, config='--psm 10 --oem 1 -c tessedit_char_whitelist=ABCDEFGHIJKLMNOPQRSTUVWXYZ')
    letter_detect = letter_detect[0]

    if letter_detect == '|':
        letter_detect = 'I'
    elif letter_detect == '0':
        letter_detect = 'O'

    final_string = final_string + letter_detect
    cv2.imwrite("letterIMGs/letter" + str(box_index) +".jpg", crop_img)
    cv2.waitKey(0)
    box_index += 1  
    
print(final_string)