# Java Neural Network from Scratch

https://github.com/user-attachments/assets/92370fa1-0391-4051-a055-8a2107c4fb88


A fully connected Neural Network implementation in Java, built from scratch without external ML libraries. It trains on the MNIST dataset to recognize handwritten digits.

## Features
- **Custom Architecture:** Built using Perceptrons, Layers, and Connections.
- **Training:** Uses Backpropagation and Gradient Descent.
- **GUI:** Includes a "Magic Slate" drawing app (`DrawApp.java`) to test the network in real-time.
- **Python Integration:** Includes scripts to visualize the dataset and preprocess inputs.

## How to Run

1. **Unzip:**
   I have provided the input.zip which includes [mnist dataset](https://www.kaggle.com/datasets/hojjatk/mnist-dataset). If you want, you can run
   ```bash
   python -u Preprocess.py
   ```
   to generate the files I placed into ```preprocessed.zip```. Also you can see samples from the preprocessed files by running
   ```bash
   python -u Visuals.py
   ```

3. **Compile the Code:**
   ```bash
   javac *.java

4. **Train the Network:**
   It is not recommended to train the model. The code solely runs on CPU and suffers from pointer chasing. It took hours to train with 10.000 samples and 5 epochs. I have already provided 2 models under ```trainedModels``` folder.
    You can use them or train with low sample and epoch values to save time.
   ```bash
   java App
   ```
   The command above will also show you the accuracy rate of our model, which is about 90% for the test data.
5. **Test Neural Network By Hand:**
   This is to see how successful our model is. Eventhough it has 90% accuracy for mnist test data, it struggles with recognizing handwritten digits. But still, performs well enough to make one believe it worked!
   ```bash
   java DrawApp
   ```


   
