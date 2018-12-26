# coding=utf-8
import matplotlib.pyplot as plt
import numpy as np
from sklearn import datasets
import tensorflow as tf
from tensorflow.python.framework import ops

ops.reset_default_graph()

# 使用sklearn随机生成200个样本点，可以根据其位置被分成两类
iris_2d, binary_target = datasets.make_classification(n_samples=200, n_features=2, n_redundant=0, n_informative=2,
                                                      n_clusters_per_class=1)

# 声明批量训练大小
batch_size = 50

# 初始化计算图
sess = tf.Session()

# 声明数据占位符
x1_data = tf.placeholder(shape=[None, 1], dtype=tf.float32)
x2_data = tf.placeholder(shape=[None, 1], dtype=tf.float32)
y_target = tf.placeholder(shape=[None, 1], dtype=tf.float32)

# 声明模型变量
# Create variables A and b (0 = x1 - A*x2 + b)
A = tf.Variable(tf.random_normal(shape=[1, 1]))
b = tf.Variable(tf.random_normal(shape=[1, 1]))

# 定义线性模型：
# 如果找到的数据点在直线以上，则将数据点代入x2-x1*A-b计算出的结果大于0；
# 同理找到的数据点在直线以下，则将数据点代入x2-x1*A-b计算出的结果小于0。
# x1 - A*x2 + b
my_mult = tf.matmul(x2_data, A)
my_add = tf.add(my_mult, b)
my_output = tf.subtract(x1_data, my_add)

# 增加TensorFlow的sigmoid交叉熵损失函数(cross entropy)
xentropy = tf.nn.sigmoid_cross_entropy_with_logits(logits=my_output, labels=y_target)

# 声明优化器方法
my_opt = tf.train.GradientDescentOptimizer(0.05)
train_step = my_opt.minimize(xentropy)

# 创建一个变量初始化操作
# tf initialize_all_variables() no long valid from
if int((tf.__version__).split('.')[1]) < 12 and int((tf.__version__).split('.')[0]) < 1:
    init = tf.initialize_all_variables()
else:
    init = tf.global_variables_initializer()
sess.run(init)

# 运行迭代1200次
for i in range(1200):
    rand_index = np.random.choice(len(iris_2d), size=batch_size)
    # rand_x = np.transpose([iris_2d[rand_index]])
    # 传入三种数据：x1,x2和y_targe
    rand_x = iris_2d[rand_index]
    rand_x1 = np.array([[x[0]] for x in rand_x])
    rand_x2 = np.array([[x[1]] for x in rand_x])
    # rand_y = np.transpose([binary_target[rand_index]])
    rand_y = np.array([[y] for y in binary_target[rand_index]])
    sess.run(train_step, feed_dict={x1_data: rand_x1, x2_data: rand_x2, y_target: rand_y})
    if (i + 1) % 200 == 0:
        print('Step #' + str(i + 1) + ' A = ' + str(sess.run(A)) + ', b = ' + str(sess.run(b)))

# 绘图

# 获取斜率/截距
[[slope]] = sess.run(A)
[[intercept]] = sess.run(b)

# 创建拟合线
x = np.linspace(-5, 5, num=50)
ablineValues = []
for i in x:
    ablineValues.append(slope * i + intercept)

# 绘制拟合曲线
class1_x = [a[1] for i, a in enumerate(iris_2d) if binary_target[i] == 1]
setosa_y = [a[0] for i, a in enumerate(iris_2d) if binary_target[i] == 1]
non_setosa_x = [a[1] for i, a in enumerate(iris_2d) if binary_target[i] == 0]
non_setosa_y = [a[0] for i, a in enumerate(iris_2d) if binary_target[i] == 0]
plt.plot(class1_x, setosa_y, 'rx', ms=10, mew=2, label='class1')
plt.plot(non_setosa_x, non_setosa_y, 'ro', label='class2')
plt.plot(x, ablineValues, 'b-')
plt.xlim([-5, 5])
plt.ylim([-5, 7.1])
plt.xlabel('X')
plt.ylabel('Y')
plt.legend(loc='lower right')
plt.show()