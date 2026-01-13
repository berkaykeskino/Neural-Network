import numpy as np
import matplotlib.pyplot as plt
from matplotlib.widgets import Button
import os

class MnistViewer:
    def __init__(self, x_path, y_path):
        self.load_data(x_path, y_path)
        
        # Setup the Plot Window
        self.fig, self.ax = plt.subplots(figsize=(7, 8))
        plt.subplots_adjust(bottom=0.2) # Leave space at the bottom for buttons
        
        # 1. Create Buttons [0] to [9]
        self.buttons = []
        btn_width = 0.08
        btn_height = 0.06
        spacing = 0.01
        
        # Calculate starting X to center the buttons
        total_width = (10 * btn_width) + (9 * spacing)
        start_x = (1.0 - total_width) / 2
        
        for i in range(10):
            # Define position [left, bottom, width, height]
            ax_btn = plt.axes([start_x + i * (btn_width + spacing), 0.05, btn_width, btn_height])
            btn = Button(ax_btn, str(i))
            
            # Use a lambda with default argument to capture the specific value of 'i'
            btn.on_clicked(lambda event, digit=i: self.show_digit(digit))
            
            self.buttons.append(btn) # Store reference so buttons don't disappear

        # Show a random "0" to start
        self.show_digit(0)
        print("\n--- MNIST Viewer Loaded ---")
        print("Click buttons on the window to load new samples.")
        plt.show()

    def load_data(self, x_path, y_path):
        if not os.path.exists(x_path) or not os.path.exists(y_path):
            raise FileNotFoundError("Could not find .bin files. Make sure xTrain.bin and yTrain.bin are here.")

        # Load Images
        raw_x = np.fromfile(x_path, dtype=np.uint8)
        self.total_images = len(raw_x) // 784
        self.images = raw_x.reshape(self.total_images, 28, 28)
        
        # Load Labels
        self.labels = np.fromfile(y_path, dtype=np.uint8)
        
        # Pre-calculate indices for each digit (Optimization)
        # digit_indices[5] will hold a list of all index positions where the label is 5
        self.digit_indices = [np.where(self.labels == i)[0] for i in range(10)]
        
        print(f"Loaded {self.total_images} images.")

    def show_digit(self, target_digit):
        # 1. Pick a random index corresponding to the requested digit
        available_indices = self.digit_indices[target_digit]
        if len(available_indices) == 0:
            print(f"No samples found for digit {target_digit}")
            return
            
        idx = np.random.choice(available_indices)
        image = self.images[idx]

        # 2. Print ASCII Debugger (Console)
        print(f"\n--- Real MNIST '{target_digit}' (Index #{idx}) ---")
        print("ASCII View (Compare thickness with your Java drawing):")
        for row in image:
            for pixel in row:
                if pixel > 128:   print(" #", end="")
                elif pixel > 30:  print(" .", end="")
                else:             print("  ", end="")
            print() 
        print("----------------------------------------------")

        # 3. Update Visual Plot (Window)
        self.ax.clear()
        
        # Standard MNIST Visualization (White Background, Black Ink)
        self.ax.imshow(image, cmap='binary', interpolation='nearest')
        
        # Re-apply Grid settings (imshow clears them)
        self.ax.grid(which='major', color='gray', linestyle='-', linewidth=0.5)
        self.ax.minorticks_on()
        # Custom ticks to align grid lines perfectly between pixels
        self.ax.set_xticks(np.arange(-0.5, 28, 1))
        self.ax.set_yticks(np.arange(-0.5, 28, 1))
        self.ax.set_xticklabels([])
        self.ax.set_yticklabels([])
        self.ax.tick_params(axis='both', which='both', length=0)

        self.ax.set_title(f"Real Training Sample: {target_digit}", fontsize=16)
        
        # Force redraw
        self.fig.canvas.draw_idle()

if __name__ == "__main__":
    # Ensure this script is in the same folder as your .bin files
    try:
        viewer = MnistViewer("xTrain.bin", "yTrain.bin")
    except Exception as e:
        print(e)