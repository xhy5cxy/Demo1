import request from '@/utils/request'

/**
 * 用户相关API
 */

// 头像上传
// 后端接口路径为 /api/avatar/upload
export function uploadAvatar(data) {
  return request({
    url: '/avatar/upload',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 删除头像
export function deleteAvatar(userId) {
  return request({
    url: `/avatar/delete/${userId}`,
    method: 'delete'
  })
}

// 获取头像信息
export function getAvatarInfo(userId) {
  return request({
    url: `/avatar/info/${userId}`,
    method: 'get'
  })
}

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/admin/users',
    method: 'get',
    params
  })
}

// 获取用户详情
export function getUserDetail(userId) {
  return request({
    url: `/admin/users/${userId}`,
    method: 'get'
  })
}

// 更新用户信息
export function updateUser(userId, data) {
  return request({
    url: `/admin/users/${userId}`,
    method: 'put',
    data
  })
}

// 更新用户状态
export function updateUserStatus(userId, status) {
  return request({
    url: `/admin/users/${userId}/status`,
    method: 'post',
    params: { status }
  })
}

// 批量更新用户状态
export function batchUpdateUserStatus(userIds, status) {
  return request({
    url: '/admin/users/batch-status',
    method: 'post',
    data: {
      userIds,
      status
    }
  })
}

// 删除用户
export function deleteUser(userId) {
  return request({
    url: `/admin/users/${userId}`,
    method: 'delete'
  })
}

// 批量删除用户
export function batchDeleteUsers(userIds) {
  return request({
    url: '/admin/users/batch-delete',
    method: 'post',
    data: { userIds }
  })
}