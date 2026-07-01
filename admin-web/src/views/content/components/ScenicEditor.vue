<template>
  <div class="scenic-editor">
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="景点名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入景点名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="位置" prop="location">
            <el-input v-model="formData.location" placeholder="请输入景点位置" />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="分类" prop="category">
            <el-select v-model="formData.category" placeholder="请选择分类" style="width: 100%">
              <el-option label="自然风光" value="自然风光" />
              <el-option label="历史古迹" value="历史古迹" />
              <el-option label="城市地标" value="城市地标" />
              <el-option label="主题公园" value="主题公园" />
              <el-option label="博物馆" value="博物馆" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio label="published">已发布</el-radio>
              <el-radio label="draft">草稿</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="景点图片">
        <image-upload v-model="formData.images" :max="10" />
      </el-form-item>
      
      <el-form-item label="景点介绍" prop="description">
        <div class="editor-container">
          <quill-editor
            ref="quillEditor"
            v-model:content="formData.description"
            :options="editorOptions"
            style="height: 400px;"
          />
        </div>
      </el-form-item>
    </el-form>
    
    <div class="editor-actions">
      <el-button type="primary" @click="handleSave">保存</el-button>
      <el-button @click="$emit('cancel')">取消</el-button>
    </div>
  </div>
</template>

<script>
import { ref, reactive, watch, onMounted } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import ImageUpload from './ImageUpload.vue'

export default {
  name: 'ScenicEditor',
  components: {
    QuillEditor,
    ImageUpload
  },
  props: {
    scenic: {
      type: Object,
      default: () => ({})
    },
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  emits: ['save', 'cancel'],
  setup(props, { emit }) {
    const formRef = ref()
    const quillEditor = ref()
    
    const formData = reactive({
      name: '',
      location: '',
      category: '',
      description: '',
      images: [],
      status: 'draft'
    })
    
    const rules = {
      name: [
        { required: true, message: '请输入景点名称', trigger: 'blur' }
      ],
      location: [
        { required: true, message: '请输入景点位置', trigger: 'blur' }
      ],
      category: [
        { required: true, message: '请选择分类', trigger: 'change' }
      ]
    }
    
    const editorOptions = {
      modules: {
        toolbar: [
          ['bold', 'italic', 'underline', 'strike'],
          ['blockquote', 'code-block'],
          [{ 'header': 1 }, { 'header': 2 }],
          [{ 'list': 'ordered' }, { 'list': 'bullet' }],
          [{ 'script': 'sub' }, { 'script': 'super' }],
          [{ 'indent': '-1' }, { 'indent': '+1' }],
          [{ 'direction': 'rtl' }],
          [{ 'size': ['small', false, 'large', 'huge'] }],
          [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
          [{ 'color': [] }, { 'background': [] }],
          [{ 'font': [] }],
          [{ 'align': [] }],
          ['clean'],
          ['link', 'image', 'video']
        ]
      },
      placeholder: '请输入景点详细介绍...',
      theme: 'snow'
    }
    
    watch(() => props.scenic, (newVal) => {
      Object.assign(formData, newVal)
    }, { immediate: true, deep: true })
    
    const handleSave = async () => {
      if (!formRef.value) return
      
      const valid = await formRef.value.validate()
      if (!valid) return
      
      emit('save', {
        ...formData,
        id: props.isEdit ? props.scenic.id : undefined
      })
    }
    
    return {
      formRef,
      quillEditor,
      formData,
      rules,
      editorOptions,
      handleSave
    }
  }
}
</script>

<style lang="scss" scoped>
.scenic-editor {
  .editor-container {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    
    :deep(.ql-editor) {
      min-height: 300px;
    }
    
    :deep(.ql-toolbar) {
      border-bottom: 1px solid #dcdfe6;
    }
  }
  
  .editor-actions {
    margin-top: 20px;
    text-align: center;
    
    .el-button {
      margin: 0 10px;
    }
  }
}
</style>