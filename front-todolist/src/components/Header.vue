<template>
  <v-app-bar
    flat
    class="border-b align-items-center d-flex"
    title="Todo-list AZ Tecnologia em Gestão"
  >
    <v-spacer></v-spacer>
    <v-text-field
      v-model="taskId"
      class="mt-5 pa-10"
      label="Digite o id da tarefa"
      clearable
      variant="solo"
      append-icon="mdi-magnify"
      style="max-width: 30%"
      @click:append="searchTask"
    />
  </v-app-bar>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {
      taskId: "",
    };
  },
  methods: {
    searchTask() {
      this.$emit("search-task", this.taskId);
    },
  },
};
</script>
