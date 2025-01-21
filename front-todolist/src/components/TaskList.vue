<template>
  <div v-if="tasks.length > 0" class="d-flex flex-column w-100">
    <v-card
      v-for="task in tasks"
      :key="task.id"
      class="my-3 pa-3 rounded-lg elevation-3 d-flex flex-row justify-between align-items-center"
      style="background-color: #424242"
    >
      <div class="d-flex flex-column">
        <h3>{{ task.title }}</h3>
        <span>{{ task.description }}</span>
      </div>

      <div class="ml-auto d-flex flex-row" style="align-items: center">
        <v-checkbox
          v-model="task.completed"
          hide-details
          @change="updateCompleteTask(task)"
          color="green"
        />
        <v-btn
          @click="openEditModal(task)"
          style="background-color: #ffb300; color: white"
        >
          <v-icon icon="mdi-pencil"></v-icon>
        </v-btn>
        <v-btn
          class="ml-3"
          @click="deleteTask(task.id)"
          style="background-color: #ef5350; color: white"
        >
          <v-icon icon="mdi-delete"></v-icon>
        </v-btn>
      </div>
    </v-card>
  </div>
  <div v-else class="mt-12 font-weight-bold text-subtitle-1">
    <p>Ainda não há tarefas criadas</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "TaskList",
  props: {
    tasks: {
      type: Array,
      required: true,
    },
  },
  methods: {
    openEditModal(task) {
      this.$emit("edit-task", task);
    },
    deleteTask(taskId) {
      this.$emit("delete-task", taskId);
    },
    async updateCompleteTask(task) {
      try {
        const response = await axios.put(
          `http://localhost:8080/api/v1/tasks/${task.id}`,
          { ...task, completed: task.completed }
        );
        this.$emit("update-task", response.data);
      } catch (error) {
        console.error("Failed at update task", error);
      }
    },
  },
};
</script>
