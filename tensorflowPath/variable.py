# coding:utf-8
import tensorflow as tf

state = tf.Variable(0, name='counter')
print(state.name)

one = tf.constant(1)

new_value = tf.add(state, one)

update = tf.assign(state, new_value)

# 定义了variable一定要通过这个初始化
init = tf.initialize_all_variables()

with tf.Session() as sess:
    # 也要通过session调用
    sess.run(init)
    for _ in range(3):
        sess.run(update)
        print(sess.run(state))