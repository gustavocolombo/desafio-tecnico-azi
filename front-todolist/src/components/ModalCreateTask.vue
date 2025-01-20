<template>
  <v-dialog v-model="isOpenModal" max-width="500px">
    <v-card>
      <v-card-title>
        <span class="headline">Criar Tarefa</span>
      </v-card-title>
      <v-card-text>
        <v-text-field v-model="localTask.title" label="Título" required />
        <v-textarea
          v-model="localTask.description"
          label="Descrição"
          required
        />
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
  name: "ModalCreateTask",
  props: {
    modelValue: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      isOpenModal: this.modelValue,
      localTask: {
        title: "",
        description: "",
      },
    };
  },
  watch: {
    modelValue(val) {
      this.isOpenModal = val;
    },
    isOpenModal(val) {
      this.$emit("update:modelValue", val);
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
