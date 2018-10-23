#ifndef ML_RWLOCK_H_
#define ML_RWLOCK_H_

#include <cstdlib>
#include <cassert>
#include <atomic>
#include <thread>
#include <mutex>
#include "raii.h"

using std::thread;
using std::atomic_int;
using std::atomic_uint;

/*
 * atomicʵ�ֶ�д��Դ��,��ռд,�����,��ֹ���ƹ��캯����'='��ֵ������
 * WRITE_FIRSTΪtrueʱΪд����ģʽ,������̵߳ȴ���ȡ(m_writeWaitCount>0)��ȴ�,������д�߳��Ȼ�ȡ��
 * ����Ƕ�׼���
 * readLock/Unlock ʵ�ֹ���Ķ�ȡ��/�������߳�������
 * writeLock/Unlock ʵ�ֶ�ռ��д���/����,ͬʱֻ����һ���߳�д�룬
 * �����߳��ڶ�ȡʱ��д���߳���������д���߳�ִ��ʱ�����еĶ�ȡ�̶߳���������
 */
class RWLock {
#define WRITE_LOCK_STATUS -1
#define FREE_STATUS 0
private:
    /* ��ʼΪ0���߳�id */
    static const  std::thread::id NULL_THEAD;
    const bool WRITE_FIRST;
    /* �����жϵ�ǰ�Ƿ���д�߳� */
    thread::id m_write_thread_id;
    /* ��Դ��������,����Ϊint��ԭ�ӳ�Ա����,-1Ϊд״̬��0Ϊ����״̬,>0Ϊ�����ȡ״̬ */
    atomic_int m_lockCount;
    /* �ȴ�д�̼߳�����,����Ϊunsigned int��ԭ�ӳ�Ա����*/
    atomic_uint m_writeWaitCount;
public:
    // ��ֹ���ƹ��캯��
    RWLock(const RWLock&) = delete;
    // ��ֹ����ֵ������
    RWLock& operator=(const RWLock&) = delete;
    //RWLock& operator=(const RWLock&) volatile = delete;
    RWLock(bool writeFirst=false);;//Ĭ��Ϊ������ģʽ
    virtual ~RWLock()=default;
    int readLock();
    int readUnlock();
    int writeLock();
    int writeUnlock();
    // ����ȡ����������ͷŶ�����װΪraii�����Զ���ɼ����ͽ�������
    raii read_guard()const noexcept{
        return make_raii(*this,&RWLock::readUnlock,&RWLock::readLock);
    }
    // ��д������������ͷŶ�����װΪraii�����Զ���ɼ����ͽ�������
    raii write_guard()noexcept{
        return make_raii(*this,&RWLock::writeUnlock,&RWLock::writeLock);
    }
};

#endif // ML_RWLOCK_H_
