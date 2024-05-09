#define STB_IMAGE_IMPLEMENTATION
#include <iostream>
#include <vector>

#include <cmath>
#include "study_polytech_scraper_phash_PHashCalculator.h"
#include "stb_image.h"

double C(int u, int N) {
    return (u == 0) ? std::sqrt(1.0 / N) : std::sqrt(2.0 / N);
}

void DCT(const std::vector<uint8_t>& image, std::vector<double>& dct, int width, int height) {
    for (int u = 0; u < height; ++u) {
        for (int v = 0; v < width; ++v) {
            double sum = 0.0;
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    double pixel = static_cast<double>(image[y * width + x]);
                    sum += pixel * std::cos((2 * x + 1) * u * M_PI / (2 * width)) * std::cos((2 * y + 1) * v * M_PI / (2 * height));
                }
            }
            sum *= C(u, width) * C(v, height);
            dct[u * width + v] = sum;
        }
    }
}

std::vector<uint8_t> loadImage(const char* filename, int &width, int &height, int &channels) {
    uint8_t* img = stbi_load(filename, &width, &height, &channels, 0);
    std::vector<uint8_t> image(img, img + width * height * channels);
    stbi_image_free(img);
    return image;
}

std::vector<uint8_t> convertToGrayscale(const std::vector<uint8_t>& image, int width, int height, int channels) {
    std::vector<uint8_t> grayscale(width * height);
    for (int i = 0; i < width * height; ++i) {
        int j = i * channels;
        uint8_t r = image[j];
        uint8_t g = image[j + 1];
        uint8_t b = image[j + 2];
        grayscale[i] = static_cast<uint8_t>(0.299 * r + 0.587 * g + 0.114 * b);
    }
    return grayscale;
}

std::vector<uint8_t> resizeImage(const std::vector<uint8_t>& image, int oldWidth, int oldHeight, int newWidth, int newHeight) {
    std::vector<uint8_t> resized(newWidth * newHeight);
    for (int y = 0; y < newHeight; ++y) {
        for (int x = 0; x < newWidth; ++x) {
            int oldX = x * oldWidth / newWidth;
            int oldY = y * oldHeight / newHeight;
            resized[y * newWidth + x] = image[oldY * oldWidth + oldX];
        }
    }
    return resized;
}

unsigned long long computeHash(const std::vector<double>& dct, int width, int height) {
    double average = 0;
    for (int i = 0; i < width * height; ++i) {
        average += dct[i];
    }
    average -= dct[0];
    average /= (width * height - 1);

    unsigned long long hash = 0;
    for (int i = 0; i < width * height; ++i) {
        if (dct[i] > average) {
            hash |= 1ULL << i;
        }
    }
    return hash;
}

unsigned long long getPHash(const char* filename) {
    int width, height, channels;
    std::vector<unsigned char> image = loadImage(filename, width, height, channels);

    image = convertToGrayscale(image, width, height, channels);
    image = resizeImage(image, width, height, 32, 32);

    std::vector<double> dct(32 * 32);
    DCT(image, dct, 32, 32);

    return computeHash(dct, 8, 8);
}


std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, nullptr);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

jlongArray Java_study_polytech_scraper_phash_PHashCalculator_calculateHashes(JNIEnv * env, jobject, jstring path) {
    std::string paths = jstring2string(env, path);
    std::cout << "Paths = " << paths << std::endl;
    std::string delimiter = "|";
    size_t pos = 0;
    std::string token;
    std::vector<unsigned long long> results;
    while ((pos = paths.find(delimiter)) != std::string::npos) {
        token = paths.substr(0, pos);
        std::cout << token << std::endl;
        unsigned long long result = getPHash(token.c_str());
        results.push_back(result);
        paths.erase(0, pos + delimiter.length());
    }
    jlong buffer[results.size()];
    std::cout << results.size() << std::endl;
    for (int i = 0; i < results.size(); i++) {
        std::cout << results[i] << std::endl;
        buffer[i] = results[i];
    }

    jlongArray jLongArray = env->NewLongArray(results.size());
    env->SetLongArrayRegion(jLongArray, 0, results.size(), buffer);
    return jLongArray;
}