<template>
  <div v-if="tasks.length > 0" class="d-flex flex-column w-100">
    <v-card
      v-for="task in tasks"
      :key="task.id"
      class="my-3 pa-3 rounded-lg elevation-3 d-flex flex-row justify-between align-items-center"
      variant="tonal"
    >
      <div class="d-flex flex-column">
        <h3>{{ task.title }}</h3>
        <span>{{ task.description }}</span>
      </div>

      <div class="ml-auto d-flex flex-row align-items-center justify-center">
        <v-checkbox :model-value="task.completed" hide-details />
        <v-btn>Editar</v-btn>
        <v-btn class="ml-3">Excluir</v-btn>
      </div>
    </v-card>
  </div>
  <div v-else>
    <p>Ainda não há tarefas criadas</p>
  </div>
</template>

<script>
export default {
  name: "TaskList",
  props: {
    tasks: {
      type: Array,
      required: true,
    },
  },
};
</script>
