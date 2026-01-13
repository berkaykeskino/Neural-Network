import numpy as np
import struct
import os

# --- Helper Functions ---
def read_labels(path):
    with open(path, 'rb') as f:
        magic, size = struct.unpack(">II", f.read(8))
        if magic != 2049: raise ValueError('Magic number mismatch')
        return np.frombuffer(f.read(), dtype=np.uint8)

def read_images(path):
    with open(path, 'rb') as f:
        magic, size, rows, cols = struct.unpack(">IIII", f.read(16))
        if magic != 2051: raise ValueError('Magic number mismatch')
        return np.frombuffer(f.read(), dtype=np.uint8).reshape(size, rows * cols)

def export_data(data, filename):
    # Ensure it's exactly uint8 (0-255)
    raw = data.astype(np.uint8).tobytes()
    with open(filename, "wb") as f:
        f.write(raw)
    print(f"Exported {len(data)} items to {filename} ({len(raw)} bytes)")

# --- Main Execution ---
base_path = os.path.dirname(os.path.abspath(__file__))
input_path = os.path.join(base_path, 'input') # Update if your path is different

# Paths to the ORIGINAL MNIST files you downloaded
train_img_path = os.path.join(input_path, 'train-images-idx3-ubyte', 'train-images-idx3-ubyte')
train_lbl_path = os.path.join(input_path, 'train-labels-idx1-ubyte', 'train-labels-idx1-ubyte')
test_img_path = os.path.join(input_path, 't10k-images-idx3-ubyte', 't10k-images-idx3-ubyte')
test_lbl_path = os.path.join(input_path, 't10k-labels-idx1-ubyte', 't10k-labels-idx1-ubyte')

# Load
x_train = read_images(train_img_path)
y_train = read_labels(train_lbl_path)
x_test = read_images(test_img_path)
y_test = read_labels(test_lbl_path)

# Export
print("--- Exporting ---")
export_data(x_train, "xTrain.bin")
export_data(y_train, "yTrain.bin")
export_data(x_test, "xTest.bin")
export_data(y_test, "yTest.bin")