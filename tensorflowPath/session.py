import tensorflow as tf

matrix1 = tf.constant([[3, 3]])
matrix2 = tf.constant([[2],
                       [2]])
product = tf.matmul(matrix1, matrix2)

# method 1
# sess = tf.Session()
# res = sess.run(product)
# print res
# sess.close()

# method 2
with tf.Session() as sess:
    res2 = sess.run(product)
    print res2
