import { createStore } from 'vuex'
import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

// 配置axios默认设置
axios.defaults.baseURL = API_BASE_URL
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default createStore({
  state: {
    user: null,
    token: localStorage.getItem('token'),
    reminders: [],
    loading: false
  },
  
  mutations: {
    SET_USER(state, user) {
      state.user = user
    },
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        localStorage.setItem('token', token)
      } else {
        localStorage.removeItem('token')
      }
    },
    SET_REMINDERS(state, reminders) {
      state.reminders = reminders
    },
    ADD_REMINDER(state, reminder) {
      state.reminders.push(reminder)
    },
    UPDATE_REMINDER(state, updatedReminder) {
      const index = state.reminders.findIndex(r => r.id === updatedReminder.id)
      if (index !== -1) {
        state.reminders.splice(index, 1, updatedReminder)
      }
    },
    DELETE_REMINDER(state, reminderId) {
      state.reminders = state.reminders.filter(r => r.id !== reminderId)
    },
    SET_LOADING(state, loading) {
      state.loading = loading
    }
  },
  
  actions: {
    async login({ commit }, credentials) {
      try {
        commit('SET_LOADING', true)
        const response = await axios.post('/users/login', credentials)
        const { token, userId, username, email } = response.data
        
        commit('SET_TOKEN', token)
        commit('SET_USER', { userId, username, email })
        
        return { success: true }
      } catch (error) {
        return { 
          success: false, 
          message: error.response?.data?.error || '登录失败' 
        }
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async register({ commit }, userData) {
      try {
        commit('SET_LOADING', true)
        await axios.post('/users/register', userData)
        return { success: true, message: '注册成功' }
      } catch (error) {
        return { 
          success: false, 
          message: error.response?.data?.error || '注册失败' 
        }
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    logout({ commit }) {
      commit('SET_TOKEN', null)
      commit('SET_USER', null)
      commit('SET_REMINDERS', [])
    },
    
    async fetchReminders({ commit, state }) {
      try {
        commit('SET_LOADING', true)
        const response = await axios.get(`/reminders/user/${state.user.userId}`)
        commit('SET_REMINDERS', response.data)
      } catch (error) {
        console.error('获取提醒失败:', error)
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async createReminder({ commit }, reminderData) {
      try {
        const response = await axios.post('/reminders', reminderData)
        commit('ADD_REMINDER', response.data)
        return { success: true }
      } catch (error) {
        return { 
          success: false, 
          message: error.response?.data?.error || '创建提醒失败' 
        }
      }
    },
    
    async updateReminder({ commit }, { id, data }) {
      try {
        const response = await axios.put(`/reminders/${id}`, data)
        commit('UPDATE_REMINDER', response.data)
        return { success: true }
      } catch (error) {
        return { 
          success: false, 
          message: error.response?.data?.error || '更新提醒失败' 
        }
      }
    },
    
    async deleteReminder({ commit }, id) {
      try {
        await axios.delete(`/reminders/${id}`)
        commit('DELETE_REMINDER', id)
        return { success: true }
      } catch (error) {
        return { 
          success: false, 
          message: error.response?.data?.error || '删除提醒失败' 
        }
      }
    }
  },
  
  getters: {
    isLoggedIn: state => !!state.token,
    user: state => state.user,
    reminders: state => state.reminders,
    loading: state => state.loading,
    activeReminders: state => state.reminders.filter(r => r.isActive),
    reminderCount: state => state.reminders.filter(r => r.isActive).length
  }
})