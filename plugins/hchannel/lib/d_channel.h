struct ByteArray {
  char *data;
  int length;
};
struct ByteArray *callNative(char *data, int length);