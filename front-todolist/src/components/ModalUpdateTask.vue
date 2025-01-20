<template>
  <v-dialog v-model="isOpenModal" max-width="500px">
    <v-card>
      <v-card-title>
        <span class="headline">Editar Tarefa</span>
      </v-card-title>
      <v-card-text>
        <v-text-field v-model="localTask.title" label="Título" />
        <v-textarea v-model="localTask.description" label="Descrição" />
        <v-checkbox v-model="localTask.completed" label="Completada" />
      </v-card-text>
      <v-card-actions>
        <v-btn class="bg-green" @click="saveTask">Salvar</v-btn>
        <v-btn class="bg-red" @click="closeModal">Cancelar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "ModalUpdateTask",
  props: {
    modelValue: {
      type: Boolean,
      required: true,
    },
    task: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      isOpenModal: this.modelValue,
      localTask: { ...this.task },
    };
  },
  watch: {
    modelValue(val) {
      this.isOpenModal = val;
    },
    isOpenModal(val) {
      this.$emit("update:modelValue", val);
    },
    task(newTask) {
      this.localTask = { ...newTask };
    },
  },
  methods: {
    saveTask() {
      this.$emit("save-task", this.localTask);
      this.closeModal();
    },
    closeModal() {
      this.isOpenModal = false;
    },
  },
};
</script>
