#!/usr/bin/python3
from prometheus_client import start_http_server, Gauge
from prometheus_client import Counter
import os
import time
import psutil

# 定义数据类型，metric，describe，标签
app_status = Gauge('AppStatus', 'Application status')
app_time = Gauge('AppTime', 'Application time')
app_cpu = Gauge('AppCpuLoad', 'Application cpu load')
dir_num = Gauge('dirNum','Calculate the number of directories',['instance'])


def get_app_status():
    pids = psutil.pids()
    for pid in pids:
        p = psutil.Process(pid)
        if p.name() == "clientApp.exe":
            if p.status() == "Running":
                app_status.set(1)
            else:
                app_status.set(0)


def get_app_time():
    pids = psutil.pids()
    for pid in pids:
        p = psutil.Process(pid)
        if p.name() == "clientApp.exe":
                app_time.set(p.create_time())


def get_app_cpu():
    pids = psutil.pids()
    for pid in pids:
        p = psutil.Process(pid)
        if p.name() == "clientApp.exe":
                app_cpu.set(p.cpu_times())


def get_dir_num():
  path = "/root/"
  count = 0
  for cdir in os.listdir(path):
    if os.path.isdir(path+cdir) and not cdir.startswith('.'):
      count += 1

  f = os.popen("hostname -i | awk '{print $2}'")
  ip = f.read().strip('\n')
  f.close()
  dir_num.labels(instance=ip).set(count)


if __name__ == "__main__":
    # 暴露端口
    start_http_server(8000)

    while True:
        get_app_status()
        time.sleep(10)
